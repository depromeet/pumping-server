package com.dpm.pumping.login

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/login")
class LoginController {

    @PostMapping("/apple")
    fun appleLogin(@RequestBody appleLoginRequest: AppleLoginRequest): String {
        // Logic for Apple Sign-In
        // Validate the Apple login request and process the login
        return "Apple login successful"
    }

    @PostMapping("/basic")
    fun basicLogin(@RequestBody basicLoginRequest: BasicLoginRequest): String {
        // Logic for Basic Login
        // Validate the basic login request and process the login
        return "Basic login successful"
    }
}
