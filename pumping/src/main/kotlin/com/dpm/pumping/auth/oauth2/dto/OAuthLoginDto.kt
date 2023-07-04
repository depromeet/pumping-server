package com.dpm.pumping.auth.oauth2.dto

import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.user.domain.CharacterType
import com.dpm.pumping.user.domain.Gender
import java.time.LocalDateTime

data class AppleLoginRequest(
    val idToken: String
)

data class SignUpRequest(
    val name: String,
    val gender: Gender,
    val height: String,
    val weight: String,
    val characterType: CharacterType,
    val loginType: LoginType,
    val oauth2Id: String?,
)

data class OAuth2LoginResponse(
    val accessToken: String?,
    val expiredTime: LocalDateTime?,
    val loginType: LoginType,
    val oauth2Id: String?
)
