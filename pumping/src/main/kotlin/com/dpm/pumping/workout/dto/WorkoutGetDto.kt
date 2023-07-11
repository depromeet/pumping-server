package com.dpm.pumping.workout.dto

class WorkoutGetDto {

    data class Response(
        val workouts: List<WorkoutByDay>?
    )

    data class WorkoutByDay(
        val workoutDate: String,
        val totalTime: Int,
        val averageHeartbeat: Int,
        val totalCalories: Int,
        val maxWorkoutCategory: String,
        val maxWorkoutCategoryTime: Int
    )
}
