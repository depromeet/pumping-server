package com.dpm.pumping.user.domain

import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.crew.Crew
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "user")
data class User(
    @Id
    var uid: String?,
    var name: String?,
    var gender: Gender?,
    var height: String?,
    var weight: String?,
    var platform: LoginPlatform,
    var currentCrew: Crew?
) {

    companion object {
        fun createWithOAuth(platform: LoginPlatform): User {
            return User(null, null, null, null, null, platform, null)
        }
    }

    fun register(
        name: String,
        gender: Gender,
        height: String,
        weight: String,
        platform: LoginPlatform
    ) {
        this.uid = UUID.randomUUID().toString()
        this.name = name
        this.gender = gender
        this.height = height
        this.weight = weight
        this.platform = platform
        this.currentCrew = null
    }
}
