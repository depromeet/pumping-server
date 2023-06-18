package com.dpm.pumping.auth.oauth2.dtos

import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.user.domain.Gender
import java.util.*

data class AppleLoginUrlResponse(
    val redirectUrl: String
)
