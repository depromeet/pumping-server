package com.dpm.pumping.user.dto

import com.dpm.pumping.user.domain.CharacterType

data class UserResponse (
    val name: String?,
    val characterType: CharacterType?,
    val currentCrew: String?,
)
