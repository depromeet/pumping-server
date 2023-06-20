package com.dpm.pumping.auth.application

import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.auth.dto.AccessTokenResponse
import com.dpm.pumping.auth.oauth2.dto.OAuth2LoginResponse
import com.dpm.pumping.auth.oauth2.dto.SignUpRequest
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.user.domain.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    fun login(appleUserId: String): OAuth2LoginResponse {

        val user = userRepository.findByPlatform(LoginPlatform(LoginType.APPLE, appleUserId))
            ?: run {
                val platform = LoginPlatform(LoginType.APPLE, appleUserId)
                userRepository.save(User.createWithOAuth(platform))
                return OAuth2LoginResponse(null, null, platform.loginType, platform.oauth2Id)
            }
        val token = jwtTokenProvider.generateAccessToken(user.uid)
        return OAuth2LoginResponse(
            accessToken = token.accessToken,
            expiredTime = token.expiredTime,
            loginType = user.platform.loginType,
            oauth2Id = user.platform.oauth2Id
        )
    }

    fun signUp(request: SignUpRequest): AccessTokenResponse {

        val platform = LoginPlatform(request.loginType, request.oauth2Id)
        val user = userRepository.findByPlatform(platform) ?: throw IllegalArgumentException(
            "인증되지 않은 사용자입니다. oauth 로그인을 다시하세요"
        )
        user.register(
            name = request.name,
            gender = request.gender,
            height = request.height,
            weight = request.weight,
            platform = platform
        )
        userRepository.save(user)
        return jwtTokenProvider.generateAccessToken(user.uid)
    }
}
