package com.dpm.pumping.user.domain

import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class UserTest {

    @Test
    fun `유저가 회원가입을 한 상태면 액세스 토큰을 발급한다`() {
        val platform = LoginPlatform(LoginType.APPLE, "appleId")
        val user = User("uid", "gc", Gender.MALE, "190", "90", platform, null)

        assertThat(user.isRegistered()).isTrue
    }

    @Test
    fun `유저 login정보를 제외한 가입 정보가 없는 상태면 등록된 상태가 아니다`() {
        val user = User.createWithOAuth(LoginPlatform(LoginType.APPLE, "appleId"))

        assertThat(user.isRegistered()).isFalse
    }
}
