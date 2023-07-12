package com.dpm.pumping.user.dto

import com.dpm.pumping.user.domain.User

data class DeleteUserEvent(
    val user: User
)
