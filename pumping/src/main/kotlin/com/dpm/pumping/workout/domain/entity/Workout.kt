package com.dpm.pumping.workout.domain.entity

import com.dpm.pumping.workout.domain.WorkoutCategory
import com.dpm.pumping.workout.dto.WorkoutCreateDto.TimerDto
import org.springframework.cglib.core.Local
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Document(collection = "workout")
data class Workout(
    @Id
    var workoutId: String,
    var userId: String,
    val currentCrew: String,
    var timers: List<Timer> = listOf(),
    var createDate: LocalDateTime
) {

    companion object {
        fun of(userId: String, currentCrew: String, timers: List<TimerDto>): Workout {
            return Workout(
                workoutId = UUID.randomUUID().toString(),
                userId = userId,
                currentCrew = currentCrew,
                timers = timers.map {
                    Timer.from(it)
                },
                createDate = LocalDateTime.now()
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
