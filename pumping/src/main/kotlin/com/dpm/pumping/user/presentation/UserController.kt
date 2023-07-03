package com.dpm.pumping.user.presentation

import com.dpm.pumping.auth.config.LoginUser
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.user.dto.UserResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/users")
@RestController
class UserController(
) {

    @GetMapping
    fun getOne(@LoginUser user: User): ResponseEntity<UserResponse> {
        val userResponse = UserResponse(
            name = user.name,
            characterType = user.characterType,
            currentCrew = user.getCrewName()
        )
        return ResponseEntity.ok(userResponse)
    }

}
