package com.dpm.pumping.auth.application

import com.dpm.pumping.auth.dtos.AccessTokenResponse
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${security.jwt.access.expire-length}") private val tokenValidityInMilliseconds: Long,
    @Value("\${security.jwt.access.secret-key}") tokenSecretKey: String
) {
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

        return AccessTokenResponse(accessToken, validity)
    }

}
