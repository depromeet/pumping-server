package com.dpm.pumping.workout.domain.entity

import com.dpm.pumping.workout.domain.WorkoutPart
import com.dpm.pumping.workout.dto.WorkoutCreateDto
import java.util.*

class Timer(
    var timerId: String,
    var workoutPart: String,
    var time: String,
    var calories: String,
    var heartbeat: String,
    var workSets: List<WorkoutSet>? = emptyList()
) {
    companion object {
        fun of(request: WorkoutCreateDto.TimerDto): Timer {
            return Timer(
                timerId = UUID.randomUUID().toString(),
                workoutPart = WorkoutPart.of(request.workoutPart).toString(),
                time = request.time.toString(),
                calories = request.calories.toString(),
                heartbeat = request.heartbeat.toString(),
                workSets = request.workoutSets?.map { WorkoutSet.of(it) }
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true

        if (other !is Timer)
            return false

        return other.timerId == timerId && other.workoutPart == workoutPart
                && other.time == time && other.calories == calories
                && other.heartbeat == heartbeat && other.workSets == workSets
    }

    override fun hashCode(): Int {
        var result = timerId.hashCode()
        result = 31 * result + workoutPart.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + calories.hashCode()
        result = 31 * result + heartbeat.hashCode()
        result = 31 * result + (workSets?.hashCode() ?: 0)
        return result
    }
}