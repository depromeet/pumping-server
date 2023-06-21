package com.dpm.pumping.auth.oauth2

import io.jsonwebtoken.Claims
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class OAuth2AppleClaimsValidator(
    @Value("\${oauth2.apple.iss}") private val iss: String,
    @Value("\${oauth2.apple.client-id}") private val clientId: String,
) {

    private val log = LoggerFactory.getLogger(OAuth2AppleClaimsValidator::class.java)

    fun isValid(claims: Claims): Boolean {
        log.info("claim iss: " + claims.issuer+" == "+iss)
        log.info("claim audience: " +claims.audience+" == "+clientId)
        return claims.issuer.contains(iss) &&
                claims.audience == clientId
    }
}
