package com.dpm.pumping.workout.repository

import com.dpm.pumping.workout.domain.WorkoutPart
import com.dpm.pumping.workout.domain.entity.Timer
import com.dpm.pumping.workout.domain.entity.Workout
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@DataMongoTest
@ActiveProfiles("test")
class WorkoutRepositoryTest(
    @Autowired val workoutRepository: WorkoutRepository
) {

    @BeforeEach
    fun setUp() {
        workoutRepository.deleteAll()
    }

    @Test
    fun testCreateRecord() {
        val timer = Timer(
            timerId = "timer01",
            workoutPart = WorkoutPart.CHEST.toString(),
            time = "60",
            calories = "100",
            heartbeat = "80"
        )

        val workout = Workout(
            workoutId = "workout01",
            userId = "user01",
            timers = listOf(timer),
            createDate = LocalDateTime.now(),
            currentCrew = "crew01"
        )

        val savedRecord = workoutRepository.save(workout)
        val foundRecord = workoutRepository.findById(savedRecord.workoutId!!)

        assertThat(foundRecord.isPresent).isTrue
        assertThat(foundRecord.get()).isEqualTo(savedRecord)
    }

    @Test
    fun deleteAllByUserId() {
        val timer = Timer(
            timerId = "timer01",
            workoutPart = WorkoutPart.CHEST.toString(),
            time = "60",
            calories = "100",
            heartbeat = "80"
        )

        val workout = Workout(
            workoutId = "workout01",
            userId = "user01",
            timers = listOf(timer),
            createDate = LocalDateTime.now(),
            currentCrew = "crew01"
        )

        val workout2 = Workout(
            workoutId = "workout02",
            userId = "user01",
            timers = listOf(timer),
            createDate = LocalDateTime.now(),
            currentCrew = "crew01"
        )

        workoutRepository.saveAll(listOf(workout, workout2))

        workoutRepository.deleteAllByUserId("user01")

        assertThat(workoutRepository.findAll()).isEmpty()
    }
}
