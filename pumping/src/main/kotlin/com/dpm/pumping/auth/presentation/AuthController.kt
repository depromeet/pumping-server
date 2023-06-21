package com.dpm.pumping.auth.presentation

import com.dpm.pumping.auth.application.AuthService
import com.dpm.pumping.auth.dto.*
import com.dpm.pumping.auth.oauth2.OAuth2AppleClient
import com.dpm.pumping.auth.oauth2.dto.AppleLoginRequest
import com.dpm.pumping.auth.oauth2.dto.AppleLoginUrlResponse
import com.dpm.pumping.auth.oauth2.dto.OAuth2LoginResponse
import com.dpm.pumping.auth.oauth2.dto.SignUpRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RequestMapping("/api/v1")
@RestController
class AuthController(
    private val oAuth2AppleClient: OAuth2AppleClient,
    private val authService: AuthService,
) {

    @PostMapping("/oauth2/apple/login")
    fun login(@RequestBody request: AppleLoginRequest): ResponseEntity<OAuth2LoginResponse> {
        val appleUserId = oAuth2AppleClient.getAppleUserId(request)
        return ResponseEntity.ok(authService.login(appleUserId))
    }

    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: SignUpRequest): ResponseEntity<AccessTokenResponse> {
        return ResponseEntity.ok(authService.signUp(request))
    }
}
