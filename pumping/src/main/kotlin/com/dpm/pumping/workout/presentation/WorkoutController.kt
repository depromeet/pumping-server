package com.dpm.pumping.workout.presentation

import com.dpm.pumping.auth.config.LoginUser
import com.dpm.pumping.workout.application.WorkoutService
import com.dpm.pumping.workout.dto.WorkoutCreateDto
import com.dpm.pumping.user.domain.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/timer")
class WorkoutController(
    private val workoutService: WorkoutService
){

    @PostMapping()
    fun createTimer(
        @RequestBody request: WorkoutCreateDto.Request, @LoginUser user: User
    ): ResponseEntity<WorkoutCreateDto.Response> {
        val response = workoutService.createWorkout(request, user)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}
