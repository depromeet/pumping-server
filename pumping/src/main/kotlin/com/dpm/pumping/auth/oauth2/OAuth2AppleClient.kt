package com.dpm.pumping.auth.oauth2

import com.dpm.pumping.auth.oauth2.dtos.AppleLoginUrlResponse
import com.dpm.pumping.auth.oauth2.vo.OAuth2ApplePublicKeys
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.nio.charset.StandardCharsets
import java.security.PublicKey
import java.util.*


@Component
class OAuth2AppleClient(
    private val claimsValidator: OAuth2AppleClaimsValidator,
    private val objectMapper: ObjectMapper,
    @Value("\${oauth2.apple.client-id}") private val clientId: String,
    @Value("\${oauth2.apple.redirect-url}") private val redirectUrl: String
) {
    private val restTemplate = RestTemplate()

    fun getRedirectUrl(): AppleLoginUrlResponse {
        return AppleLoginUrlResponse(String.format(AUTH_URL, clientId, redirectUrl))
    }

    fun getAppleUserId(idToken: String): String {
        val extractHeader = extractHeader(idToken)
        val generatePublicKey = OAuth2AppleKeyGenerator.generatePublicKey(extractHeader, getOAuth2AppleKeys())
        val claims: Claims = parsePublicKeyAndGetClaims(idToken, generatePublicKey)
        validateClaims(claims)
        return claims.subject
    }

    private fun extractHeader(idToken: String): Map<String, String> {
        try {
            val encodedHeader: String = idToken.split(IDENTITY_TOKEN_DELIMITER)[HEADER_INDEX]
            val decodedBytes = Base64.getDecoder().decode(encodedHeader)
            val decodedHeader = String(decodedBytes, StandardCharsets.UTF_8)
            return objectMapper.readValue(decodedHeader)
        } catch (e: JsonProcessingException) {
            throw IllegalArgumentException("Invalid Apple OAuth Identity Token", e)
        } catch (e: ArrayIndexOutOfBoundsException) {
            throw IllegalArgumentException("Invalid Apple OAuth Identity Token", e)
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
        if (response.body == null) {
            throw IllegalStateException("응답값에 null이 들어옵니다.")
        }
        return response.body!!
    }

    fun parsePublicKeyAndGetClaims(idToken: String, publicKey: PublicKey): Claims {
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
        private const val IDENTITY_TOKEN_DELIMITER = "\\."
        private const val HEADER_INDEX = 0
        private const val AUTH_URL: String =
            "https://appleid.apple.com/auth/authorize?client_id=%s&redirect_uri=%s&response_type=code id_token&response_mode=fragment"
    }
}
