package com.dpm.pumping.auth.presentation

import com.dpm.pumping.auth.application.AuthService
import com.dpm.pumping.auth.dtos.*
import com.dpm.pumping.auth.oauth2.OAuth2AppleClient
import com.dpm.pumping.auth.oauth2.dtos.AppleLoginRequest
import com.dpm.pumping.auth.oauth2.dtos.AppleLoginUrlResponse
import com.dpm.pumping.auth.oauth2.dtos.OAuth2LoginResponse
import com.dpm.pumping.auth.oauth2.dtos.SignUpRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RequestMapping("/api/v1")
@RestController
class AuthController(
    private val oAuth2AppleClient: OAuth2AppleClient,
    private val authService: AuthService,
) {

    @GetMapping("/oauth2/apple")
    fun transferLoginUrl(): ResponseEntity<AppleLoginUrlResponse> {
        return ResponseEntity.ok(oAuth2AppleClient.getRedirectUrl())
    }

    @PostMapping("/oauth2/apple/login")
    fun login(@RequestBody request: AppleLoginRequest): ResponseEntity<OAuth2LoginResponse> {
        val appleUserId = oAuth2AppleClient.getAppleUserId(request.idToken)
        return ResponseEntity.ok(authService.login(appleUserId))
    }

    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: SignUpRequest): ResponseEntity<AccessTokenResponse> {
        return ResponseEntity.ok(authService.signUp(request))
    }
}