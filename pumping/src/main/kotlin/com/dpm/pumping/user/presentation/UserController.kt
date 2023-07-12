package com.dpm.pumping.user.presentation

import com.dpm.pumping.auth.config.LoginUser
import com.dpm.pumping.user.application.UserService
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.user.dto.UserResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/users")
@RestController
class UserController(
    private val userService: UserService
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

    @DeleteMapping
    fun delete(@LoginUser user: User): ResponseEntity<Void> {
        userService.delete(user)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

}
