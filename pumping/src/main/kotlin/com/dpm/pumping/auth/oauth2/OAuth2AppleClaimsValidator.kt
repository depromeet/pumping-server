package com.dpm.pumping.auth.oauth2

import io.jsonwebtoken.Claims
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class OAuth2AppleClaimsValidator(
    @Value("\${oauth2.apple.iss}") private val iss: String,
    @Value("\${oauth2.apple.client-id}") private val clientId: String,
    @Value("\${oauth2.apple.nonce}") nonce: String
) {
    private val nonce: String

    init {
        this.nonce = EncryptUtils.encrypt(nonce)
    }

    fun isValid(claims: Claims): Boolean {
        return claims.issuer.contains(iss) &&
                claims.audience == clientId &&
                claims.get("nonce", String::class.java) == nonce
    }
}