package com.dpm.pumping.workout.application

import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.user.domain.Gender
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.workout.dto.WorkoutCreateDto
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class WorkoutServiceTest(
    @Autowired private val workoutService: WorkoutService
) {
    var testUser = User(
            uid = "user01",
            name = "name",
            gender = Gender.FEMALE,
            height = "160",
            weight = "50",
            platform = LoginPlatform(LoginType.APPLE, "oauth2Id"),
            currentCrew = null
        )

    @Test
    fun 운동_기록에_성공한다() {
        val request = WorkoutCreateDto.Request(
            timers = listOf(
                WorkoutCreateDto.TimerDto(
                    time = 0,
                    heartbeat = 0,
                    calories = 0,
                    workoutPart = "가슴",
                    workoutSets = listOf(
                        WorkoutCreateDto.WorkoutSetDto(
                            machine = "체스트 프레스",
                            kg = 100,
                            sets = 3
                        )
                    )
                )
            )
        )

        val response = workoutService.createWorkout(request, testUser)

        Assertions.assertThat(response.uid).isNotNull
    }
}