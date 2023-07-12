package com.dpm.pumping.workout.application

import com.dpm.pumping.workout.domain.entity.Workout
import java.time.LocalDate

data class WorkoutStorage(
    private val crewCreatedAt: LocalDate
) {

    companion object {
        const val DEFAULT_SIZE = 7L
    }

    private val storage = mutableMapOf<LocalDate, WorkoutByDay?>()

    init {
        for (i in 0 until DEFAULT_SIZE) {
            val key = crewCreatedAt.plusDays(i)
            storage[key] = null
        }
    }

    fun save(workout: Workout) {
        val key = workout.createDate.toLocalDate()
        val data = WorkoutByDay.from(workout, crewCreatedAt)
        storage[key] = data
    }

    data class WorkoutByDay(
        val workoutDate: String,
        val totalTime: Int,
        val averageHeartbeat: Int,
        val totalCalories: Int,
        val maxWorkoutCategory: String,
        val maxWorkoutCategoryTime: Int
    ) {
        companion object {
            fun from(workout: Workout, crewCreatedAt: LocalDate): WorkoutByDay {
                val maxWorkoutData = workout.getMaxWorkoutPart()
                return WorkoutByDay(
                    workoutDate = workout.calculateDays(crewCreatedAt).toString(),
                    totalTime = workout.getTotalTime(),
                    averageHeartbeat = workout.getAverageHeartbeat(),
                    totalCalories = workout.getTotalCalories(),
                    maxWorkoutCategory = maxWorkoutData.first.name,
                    maxWorkoutCategoryTime = maxWorkoutData.second
                )
            }
        }
    }

    fun get(): Map<LocalDate, WorkoutByDay?> {
        return storage.toMap()
    }
}
