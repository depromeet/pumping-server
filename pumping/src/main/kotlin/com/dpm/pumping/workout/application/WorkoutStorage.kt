package com.dpm.pumping.workout.application

import com.dpm.pumping.workout.domain.entity.Workout
import java.time.LocalDate

data class WorkoutStorage(
    private val crewCreatedAt: LocalDate
) {

    companion object {
        const val DEFAULT_SIZE = 7L
    }

    private val storage = mutableMapOf<Date, Workout?>()

    init {
        for (i in 0 until DEFAULT_SIZE) {
            val key = Date(crewCreatedAt.plusDays(i), (i+1).toInt())
            storage[key] = null
        }
    }

    fun save(workout: Workout) {
        val workoutDate = workout.createDate.toLocalDate()
        val workoutDayCount = workout.calculateDays(crewCreatedAt)
        val key = Date(workoutDate, workoutDayCount)
        storage[key] = workout
    }

    fun get(): Map<Date, Workout?> {
        return storage.toMap()
    }

    data class Date(
        val workoutDate: LocalDate,
        val workoutDayCount: Int
    ) {

        override fun equals(other: Any?): Boolean {
            if (other === this)
                return true

            if (other !is Date)
                return false

            return other.workoutDate == workoutDate
                    && other.workoutDayCount == workoutDayCount
        }

        override fun hashCode(): Int {
            var result = workoutDate.hashCode()
            result = 31 * result + workoutDayCount
            return result
        }
    }
}
