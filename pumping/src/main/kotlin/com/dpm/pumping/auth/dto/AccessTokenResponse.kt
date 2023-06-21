package com.dpm.pumping.auth.dto

import java.util.*

data class AccessTokenResponse(
    val accessToken: String?,
    val expiredTime: Date?,
)
