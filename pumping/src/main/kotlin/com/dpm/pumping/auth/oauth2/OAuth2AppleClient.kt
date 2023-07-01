package com.dpm.pumping.auth.oauth2

import com.dpm.pumping.auth.oauth2.dto.AppleLoginRequest
import com.dpm.pumping.auth.oauth2.vo.OAuth2ApplePublicKeys
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.security.PublicKey
import java.util.*


@Component
class OAuth2AppleClient(
    private val claimsValidator: OAuth2AppleClaimsValidator,
    private val objectMapper: ObjectMapper,
) {
    private val restTemplate = RestTemplate()

    fun getAppleUserId(request: AppleLoginRequest): String {
        val extractHeader = extractHeader(request.idToken)
        val generatePublicKey = OAuth2AppleKeyGenerator.generatePublicKey(extractHeader, getOAuth2AppleKeys())
        val claims: Claims = parsePublicKeyAndGetClaims(request.idToken, generatePublicKey)
        validateClaims(claims)
        return claims.subject
    }

    private fun extractHeader(idToken: String): Map<String, String> {
        try {
            val encodedHeader: String = idToken.split(IDENTITY_TOKEN_DELIMITER)[HEADER_INDEX]
            val decodedBytes = String(Base64.getDecoder().decode(encodedHeader))
            return objectMapper.readValue(decodedBytes)
        } catch (e: JsonProcessingException) {
            throw IllegalArgumentException("json 처리 도중 예외가 발생했습니다.", e)
        } catch (e: ArrayIndexOutOfBoundsException) {
            throw IllegalArgumentException("배열의 index가 범위를 벗어납니다.", e)
        }
    }

    private fun getOAuth2AppleKeys(): OAuth2ApplePublicKeys {
        val url = "https://appleid.apple.com/auth/keys"
        val response: ResponseEntity<OAuth2ApplePublicKeys> = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            OAuth2ApplePublicKeys::class.java
        )

        require(response.statusCode.is2xxSuccessful)
        return response.body ?: throw IllegalStateException("응답값에 null이 들어옵니다.")
    }

    private fun parsePublicKeyAndGetClaims(idToken: String, publicKey: PublicKey): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(publicKey)
            .build()
            .parseClaimsJws(idToken)
            .body
    }

    private fun validateClaims(claims: Claims) {
        if (!claimsValidator.isValid(claims)) {
            throw IllegalArgumentException("Apple OAuth Claims값이 올바르지 않습니다.")
        }
    }

    companion object {
        private const val IDENTITY_TOKEN_DELIMITER = "."
        private const val HEADER_INDEX = 0
    }
}
