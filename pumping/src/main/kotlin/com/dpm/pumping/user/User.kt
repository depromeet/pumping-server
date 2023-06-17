package com.dpm.pumping.user

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(
    val uid: String,
    val loginPlatform: LoginPlatform,
    val nickname: String,
    val gender: String,
    val height: String,
    val weight: String
)

data class LoginPlatform(
    val oauth2id: String,
    val loginType: String
)
