package com.dpm.pumping.workout.presentation

import com.dpm.pumping.auth.config.LoginUser
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.workout.application.WorkoutService
import com.dpm.pumping.workout.dto.WorkoutCreateDto
import com.dpm.pumping.workout.dto.WorkoutGetDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/workout")
class WorkoutController(
    private val workoutService: WorkoutService
){

    @PostMapping
    fun createTimer(
        @RequestBody request: WorkoutCreateDto.Request, @LoginUser user: User
    ): ResponseEntity<WorkoutCreateDto.Response> {
        val response = workoutService.createWorkout(request, user)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/", "/{userId}")
    fun getTimers(
        @PathVariable("userId", required = false) userId: String?,
        @LoginUser loginUser: User
    ): ResponseEntity<WorkoutGetDto.Response> {
        val response = workoutService.getWorkouts(userId, loginUser)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }
}
