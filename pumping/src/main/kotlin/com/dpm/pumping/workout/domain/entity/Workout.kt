package com.dpm.pumping.workout.domain.entity

import com.dpm.pumping.workout.dto.WorkoutCreateDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "workout")
data class Workout(
    @Id
    var workoutId: String,
    var userId: String,
    var timers: List<Timer> = listOf()
) {

    companion object {
        fun of(userId: String, request: WorkoutCreateDto.Request): Workout {
            return Workout(
                workoutId = UUID.randomUUID().toString(),
                userId = userId,
                timers = request.timers.map {
                    Timer.from(it)
                }
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true

        if (other !is Workout)
            return false

        return other.workoutId == workoutId && other.userId == userId
                && other.timers == timers
    }

    override fun hashCode(): Int {
        var result = workoutId.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + timers.hashCode()
        return result
    }
}