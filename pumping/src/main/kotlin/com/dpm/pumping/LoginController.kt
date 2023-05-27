package com.dpm.pumping

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@RestController
class LoginController {

    @GetMapping("/login")
    fun showLoginForm(): String {
        return "login"
    }

    @PostMapping("/login")
    fun processLogin(): String {
        // @RequestParam("username") username: String,
        // @RequestParam("password") password: String,
        return "login"
    }
}
