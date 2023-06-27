package com.dpm.pumping.workout.application

import com.dpm.pumping.user.domain.User
import com.dpm.pumping.workout.domain.entity.Workout
import com.dpm.pumping.workout.dto.WorkoutCreateDto
import com.dpm.pumping.workout.repository.WorkoutRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class WorkoutService(
    private val workoutRepository: WorkoutRepository
){

    @Transactional
    fun createWorkout(
        request: WorkoutCreateDto.Request, user: User
    ): WorkoutCreateDto.Response {
        val workout = Workout.of(user.uid!!, request)
        val created = workoutRepository.save(workout)
        return WorkoutCreateDto.Response(created.workoutId)
    }
}