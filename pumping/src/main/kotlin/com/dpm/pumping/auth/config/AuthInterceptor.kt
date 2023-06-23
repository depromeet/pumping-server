package com.dpm.pumping.auth.config

import com.dpm.pumping.auth.application.JwtTokenProvider
import com.dpm.pumping.auth.util.TokenParser
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor(
    private val jwtTokenProvider: JwtTokenProvider
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val accessToken: String = TokenParser.extract(request.getHeader(HttpHeaders.AUTHORIZATION))
        validateAccessToken(accessToken)
        val uid = jwtTokenProvider.getAccessTokenPayload(accessToken)
        request.setAttribute("PAYLOAD", uid)
        return true
    }

    private fun validateAccessToken(accessToken: String) {
        if (!jwtTokenProvider.isValidAccessToken(accessToken)) {
            throw IllegalArgumentException("유효하지 않은 토큰입니다. : $accessToken")
        }
    }
}
