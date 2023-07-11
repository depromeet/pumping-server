package com.dpm.pumping.user.application

import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.crew.Crew
import com.dpm.pumping.crew.CrewRepository
import com.dpm.pumping.user.domain.CharacterType
import com.dpm.pumping.user.domain.Gender
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.user.domain.UserRepository
import com.dpm.pumping.workout.application.WorkoutService
import com.dpm.pumping.workout.application.WorkoutServiceTest
import com.dpm.pumping.workout.domain.entity.Workout
import com.dpm.pumping.workout.dto.WorkoutCreateDto
import com.dpm.pumping.workout.repository.WorkoutRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.transaction.TestTransaction
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userService: UserService,
    private val crewRepository: CrewRepository,
    private val userRepository: UserRepository,
    private val workoutRepository: WorkoutRepository
){


    lateinit var crew: Crew
    lateinit var user1: User
    lateinit var user2: User

    @BeforeEach
    fun setUp(){
        userRepository.deleteAll()
        crewRepository.deleteAll()

        user1 = User(
            uid = "123",
            name = "Hihi",
            gender = Gender.MALE,
            height = "12",
            weight = "12",
            platform = LoginPlatform(LoginType.APPLE, "oauthID"),
            characterType = CharacterType.A,
            null
        )

        user2 = User(
            uid = "987",
            name = "Hihi",
            gender = Gender.MALE,
            height = "12",
            weight = "12",
            platform = LoginPlatform(LoginType.APPLE, "oauthID"),
            characterType = CharacterType.A,
            null
        )


        crew = Crew(
            crewId = "crew-1",
            crewName = "CREWNAME",
            code = "123456",
            createDate = "2024-03-04",
            goalCount = 5,
            participants = listOf(user1.uid, user2.uid)
        )

        user1.currentCrew = crew
        user2.currentCrew = crew

        user1 = userRepository.save(user1)
        user2 = userRepository.save(user2)
        crewRepository.save(crew)
    }

    @Test
    fun delete() {
        userService.delete(user1)

        Thread.sleep(2000)
        val participants = crewRepository.findByCrewId(crewId = crew.crewId!!)!!.participants

        assertThat(participants.contains(user1.uid)).isFalse
    }
}
