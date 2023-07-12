package com.dpm.pumping.workout.dto

import com.dpm.pumping.workout.application.WorkoutStorage

class WorkoutGetDto {

    data class Response(
        val workouts: List<WorkoutResponse>
    )

    data class WorkoutResponse(
        val dayOfWeek: String,
        val workout: WorkoutStorage.WorkoutByDay?
    )
}
