package com.dpm.pumping.crew

import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.crew.dto.CreateCrewRequest
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.user.domain.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class CrewServiceTest @Autowired constructor(
    private val crewRepository: CrewRepository,
    private val crewService: CrewService,
    private val userRepository: UserRepository
){

    lateinit var crew : Crew
    lateinit var user: User
    @BeforeEach
    fun setUp(){
        userRepository.deleteAll()
        crewRepository.deleteAll()

        user = userRepository.save(User.createWithOAuth(LoginPlatform(LoginType.APPLE, "OAUTH_ID")))
        crew = Crew.create("new crew", 5, user.uid!!)
        crewRepository.save(crew)
    }

    @Test
    fun `크루 생성 테스트`() {
        val crewResponse = crewService.create(CreateCrewRequest("HI crew", 5), user)

        assertThat(crewResponse.participants).contains(user.uid)
        assertThat(crewResponse.goalCount).isEqualTo(5)
    }

    @Test
    fun `크루 탈퇴 테스트`(){
        val crewResponse = crewService.create(CreateCrewRequest("HI crew", 5), user)
        val response = crewService.leaveCrew(crewResponse.crewId!!, user)

        assertThat(response.participants).doesNotContain(user.uid)
    }

    @Test
    fun `코드로 크루 참여 테스트`() {
        val newUser = userRepository.save(User.createWithOAuth(LoginPlatform(LoginType.APPLE, "OAUTH_ID2")))

        val crewResponse = crewService.create(CreateCrewRequest("HI crew", 5), user)
        val response = crewService.joinCrew(crewResponse.code!!, newUser)

        assertThat(response.participants).contains(newUser.uid)
        assertThat(response.participants.size).isEqualTo(2)
        assertThat(response.goalCount).isEqualTo(5)

        crewRepository.findAll().stream().forEach { println(it) }
    }
}
