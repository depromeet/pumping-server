package com.dpm.pumping.auth.application

import com.dpm.pumping.auth.dto.AccessTokenResponse
import com.dpm.pumping.auth.oauth2.OAuth2AppleClaimsValidator
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${security.jwt.access.expire-length}") private val tokenValidityInMilliseconds: Long,
    @Value("\${security.jwt.access.secret-key}") tokenSecretKey: String
) {
    private val log = LoggerFactory.getLogger(JwtTokenProvider::class.java)

    private val tokenSecretKey: Key

    init {
        this.tokenSecretKey = Keys.hmacShaKeyFor(tokenSecretKey.toByteArray(StandardCharsets.UTF_8))
    }

    fun generateAccessToken(payload: String?): AccessTokenResponse {
        val claims = Jwts.claims().setSubject(payload)
        val now = Date()
        val validity = Date(now.time + tokenValidityInMilliseconds)
        val accessToken = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(tokenSecretKey, SignatureAlgorithm.HS256)
            .compact()

        return AccessTokenResponse(accessToken, localDateTime(validity))
    }

    private fun localDateTime(validity: Date): LocalDateTime? =
        LocalDateTime.ofInstant(validity.toInstant(), ZoneId.of("Asia/Seoul"))

    fun getAccessTokenPayload(token: String): String {
        return getClaimsJws(token, tokenSecretKey)
            .body
            .subject
    }

    fun isValidAccessToken(token: String): Boolean {
        return try {
            val claims = getClaimsJws(token, tokenSecretKey)
            !claims.body
                .expiration
                .before(Date())
        } catch (e: Exception) {
            log.info("토큰($token)이 유효하지 않습니다:$e")
            false
        }
    }

    private fun getClaimsJws(token: String, secretKey: Key): Jws<Claims> {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
    }

}
