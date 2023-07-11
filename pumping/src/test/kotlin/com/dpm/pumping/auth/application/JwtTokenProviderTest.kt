package com.dpm.pumping.auth.application

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.*

@ActiveProfiles("test")
@SpringBootTest
class JwtTokenProviderTest(
    @Autowired val jwtTokenProvider: JwtTokenProvider
) {

    @Test
    fun `액세스 토큰의 만료시간이 1초이다`() {

        val accessToken = jwtTokenProvider.generateAccessToken("asd")

        assertThat(accessToken.expiredTime!!.isAfter(LocalDateTime.now())).isTrue
    }

}
