package com.dpm.pumping.auth.oauth2.vo

data class OAuth2ApplePublicKey(
    val kty: String?,
    val kid: String?,
    val use: String?,
    val alg: String?,
    val n: String?,
    val e: String?
)