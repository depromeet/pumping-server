package com.dpm.pumping.workout.dto

class WorkoutGetDto {

    data class Response(
        val workouts: List<WorkoutResponse>
    )

    data class WorkoutResponse(
        val dayOfWeek: String,
        val workout: DailyWorkout?
    )

    data class DailyWorkout(
        val workoutDate: String,
        var totalTime: Int,
        var averageHeartbeat: Int,
        var totalCalories: Int,
        var maxWorkoutCategory: String?,
        var maxWorkoutCategoryTime: Int
    )
}
