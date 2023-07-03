package com.dpm.pumping.workout.dto


class WorkoutCreateDto {

    data class Request(
        val timers: List<TimerDto>
    )

    data class TimerDto(
        val time: Int,
        val heartbeat: Int,
        val calories: Int,
        val workoutPart: String,
        val workoutSets: List<WorkoutSetDto>?
    )

    data class WorkoutSetDto(
        val machine: String,
        val kg: Int,
        val sets: Int
    )

    data class Response(
        val uid: String
    )
}
