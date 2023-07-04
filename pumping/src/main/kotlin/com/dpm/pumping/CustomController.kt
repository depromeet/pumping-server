package com.dpm.pumping

import com.dpm.pumping.auth.application.JwtTokenProvider
import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.auth.dto.AccessTokenResponse
import com.dpm.pumping.user.domain.CharacterType
import com.dpm.pumping.user.domain.Gender
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.user.domain.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class CustomController (
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider

){

    @PostMapping
    fun tmp(): ResponseEntity<AccessTokenResponse> {
        val user = userRepository.save(
            User(
                uid = null,
                name = "test",
                gender = Gender.MALE,
                height = "190",
                weight = "90",
                platform = LoginPlatform(LoginType.APPLE, "OAUTH_ID"),
                characterType = CharacterType.A,
                currentCrew = null
            )
        )
        return ResponseEntity.ok(jwtTokenProvider.generateAccessToken(user.uid))
    }
}
