package com.dpm.pumping.login

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController {

    @GetMapping("/login")
    fun getLogin(): String {
        return "Get Login"
    }

    @PostMapping("/login")
    fun processLogin(@RequestBody loginRequest: LoginRequest): String {
        return "Post Login"
    }

    data class LoginRequest(val username: String, val password: String)

    data class Error(val code: Int, val message: String)
}
