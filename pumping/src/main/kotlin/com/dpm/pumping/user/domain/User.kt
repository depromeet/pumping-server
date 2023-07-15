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
    var characterType: CharacterType?,
    var currentCrew: String?
) {

    companion object {
        fun createWithOAuth(platform: LoginPlatform): User {
            return User(
                uid = UUID.randomUUID().toString(),
                name = null,
                gender = null,
                height = null,
                weight = null,
                platform = platform,
                characterType = null,
                currentCrew = null
            )
        }
    }

    fun isRegistered(): Boolean {
        return (name != null && gender != null && height != null && weight != null && characterType != null)
    }

    fun getCrewName(): String {
        return currentCrew ?: throw IllegalStateException("현재 속한 크루가 없습니다.")
    }

    fun update(
        name: String,
        gender: Gender,
        height: String,
        weight: String,
        characterType: CharacterType,
        platform: LoginPlatform
    ){
        this.name = name
        this.gender = gender
        this.height = height
        this.weight = weight
        this.characterType = characterType
        this.platform = platform
    }

}
