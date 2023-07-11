package com.dpm.pumping.workout.repository

import com.dpm.pumping.workout.domain.entity.Workout
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.LocalDateTime

interface WorkoutRepository : MongoRepository<Workout, String> {

    fun findAllByCurrentCrewAndUserIdAndCreateDateBetween(
        crewId: String, userId: String, startDate: LocalDateTime, endDate: LocalDateTime
    ): List<Workout>?

    fun deleteAllByUserId(userId: String)
}
