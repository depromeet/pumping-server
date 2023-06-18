package com.dpm.pumping.auth.oauth2

import com.dpm.pumping.auth.oauth2.dtos.AppleLoginUrlResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.nio.charset.StandardCharsets
import java.util.*


@Component
class OAuth2AppleClient(
    @Value("\${oauth2.apple.client-id}") private val clientId: String,
    @Value("\${oauth2.apple.redirect-url}") private val redirectUrl: String
) {
    private val restTemplate = RestTemplate()

    fun getRedirectUrl(): AppleLoginUrlResponse {
        return AppleLoginUrlResponse(String.format(AUTH_URL, clientId, redirectUrl))
    }


    companion object {
        private const val IDENTITY_TOKEN_DELIMITER = "\\."
        private const val HEADER_INDEX = 0
        private const val AUTH_URL: String =
            "https://appleid.apple.com/auth/authorize?client_id=%s&redirect_uri=%s&response_type=code id_token&response_mode=fragment"
    }
}