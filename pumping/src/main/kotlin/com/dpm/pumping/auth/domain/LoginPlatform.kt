package com.dpm.pumping.auth.domain

data class LoginPlatform(
    val loginType: LoginType,
    val oauth2Id: String?
)
