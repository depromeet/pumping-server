package com.dpm.pumping.auth.application

import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.auth.oauth2.dto.SignUpRequest
import com.dpm.pumping.user.domain.CharacterType
import com.dpm.pumping.user.domain.Gender
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.user.domain.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AuthServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val authService: AuthService,
){

    @BeforeEach
    fun `tear down`(){
        userRepository.deleteAll()
    }

    @Test
    fun `최초 로그인한 사용자면 token값으로 null을 반환한다`() {
        val response = authService.login("appleUserId")

        assertThat(response.accessToken).isNull()
        assertThat(response.expiredTime).isNull()
        assertThat(response.loginType).isEqualTo(LoginType.APPLE)
    }

    @Test
    fun `기존에 로그인한 사용자여도 회원가입이 안되어있으면 token값으로 null을 반환한다`() {
        val savedUser = userRepository.save(User.createWithOAuth(LoginPlatform(LoginType.APPLE, "OauthId")))

        val token = authService.login(savedUser.platform.oauth2Id!!)

        assertThat(token.accessToken).isNull()
        assertThat(token.expiredTime).isNull()
    }

    @Test
    fun `애플 로그인으로 인증되지 않은 사용자가 회원가입을 시도하면 예외가 발생한다`() {

        assertThatThrownBy { authService.signUp(SignUpRequest("haha", Gender.MALE, "12", "13.3", CharacterType.A, LoginType.APPLE, null)) }
            .hasMessageContaining("인증되지 않은")
    }

    @Test
    fun `회원가입에 성공하면 accessToken을 반환한다`() {
        val loginResponse = authService.login("apple")
        val response =
            authService.signUp(SignUpRequest("haha", Gender.MALE, "12", "43", CharacterType.A, loginResponse.loginType, loginResponse.oauth2Id))

        assertThat(response.accessToken).isNotNull
        assertThat(response.expiredTime).isNotNull
    }
}
