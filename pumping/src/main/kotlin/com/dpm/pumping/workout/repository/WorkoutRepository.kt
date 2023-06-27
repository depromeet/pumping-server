package com.dpm.pumping.workout.repository

import com.dpm.pumping.workout.domain.entity.Workout
import org.springframework.data.mongodb.repository.MongoRepository

interface WorkoutRepository : MongoRepository<Workout, String> {
    fun findByUserId(userId: String): List<Workout>
}
