package com.dpm.pumping.workout.repository

import com.dpm.pumping.workout.domain.WorkoutPart
import com.dpm.pumping.workout.domain.entity.Timer
import com.dpm.pumping.workout.domain.entity.Workout
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest

@DataMongoTest
class WorkoutRepositoryTest(
    @Autowired val workoutRepository: WorkoutRepository,
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
            time = "2023-06-22T10:00:00+09:00",
            calories = "100",
            heartbeat = "80"
        )

        val workout = Workout(
            workoutId = "workout01",
            userId = "user01",
            timers = listOf(timer)
        )

        val savedRecord = workoutRepository.save(workout)
        val foundRecord = workoutRepository.findById(savedRecord.workoutId)

        assertThat(foundRecord.isPresent).isTrue
        assertThat(foundRecord.get()).isEqualTo(savedRecord)
    }
}
