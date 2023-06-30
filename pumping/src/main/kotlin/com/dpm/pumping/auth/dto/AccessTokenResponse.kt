package com.dpm.pumping.auth.dto

import java.time.LocalDateTime

data class AccessTokenResponse(
    val accessToken: String?,
    val expiredTime: LocalDateTime?,
)
