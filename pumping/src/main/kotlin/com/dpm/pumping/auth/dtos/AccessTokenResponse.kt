package com.dpm.pumping.auth.dtos

import java.util.*

data class AccessTokenResponse(
    val accessToken: String?,
    val expiredTime: Date?,
)
