package com.dpm.pumping.login

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController {

    @GetMapping("/login")
    fun getLogin(): String {
        return "Get Login"
    }

    @PostMapping("/login")
    fun processLogin(): String {
        // @RequestParam("username") username: String,
        // @RequestParam("password") password: String,
        return "Post Login"
    }
}