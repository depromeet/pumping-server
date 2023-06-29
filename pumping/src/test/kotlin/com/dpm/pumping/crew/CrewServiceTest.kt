package com.dpm.pumping.crew

import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.crew.dto.CreateCrewRequest
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.user.domain.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CrewServiceTest @Autowired constructor(
    private val crewRepository: CrewRepository,
    private val crewService: CrewService,
    private val userRepository: UserRepository
){

    @Test
    fun `크루 생성 테스트`() {
        val user = userRepository.save(User.createWithOAuth(LoginPlatform(LoginType.APPLE, "OAUTH_ID")))
        Crew.create("new crew", 5, user.uid!!)

        val crewResponse = crewService.create(CreateCrewRequest("new crew", 5), user)

        assertThat(crewResponse.participants).contains(user.uid)
        assertThat(crewResponse.goalCount).isEqualTo(5)
    }
}
