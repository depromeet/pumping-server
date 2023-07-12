package com.dpm.pumping.auth.dto

import java.time.LocalDateTime

data class SignUpResponse(
    val accessToken: String?,
    val expiredTime: LocalDateTime?,
    val userId: String
) {

    constructor(accessTokenResponse: AccessTokenResponse, userId: String) :
            this(
                accessToken = accessTokenResponse.accessToken,
                expiredTime = accessTokenResponse.expiredTime,
                userId = userId
            )

}
