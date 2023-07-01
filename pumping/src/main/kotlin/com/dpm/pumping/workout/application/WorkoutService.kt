package com.dpm.pumping.workout.application

import com.dpm.pumping.user.domain.User
import com.dpm.pumping.workout.domain.entity.Workout
import com.dpm.pumping.workout.dto.WorkoutCreateDto
import com.dpm.pumping.workout.dto.WorkoutGetDto
import com.dpm.pumping.workout.repository.WorkoutRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
@Transactional(readOnly = true)
class WorkoutService(
    private val workoutRepository: WorkoutRepository
){

    companion object {
        private const val DEFAULT_CREW_DURATION = 7L
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }

    @Transactional
    fun createWorkout(
        request: WorkoutCreateDto.Request, user: User
    ): WorkoutCreateDto.Response {
        val workout = Workout.of(user.uid!!, request.currentCrew, request.timers)
        val created = workoutRepository.save(workout)
        return WorkoutCreateDto.Response(created.workoutId)
    }

    fun getWorkouts(user: User): WorkoutGetDto.Response {
        val crew = user.currentCrew
            ?: throw IllegalArgumentException("아직 크루에 참여하지 않아 운동 기록이 존재하지 않습니다.")

        val startDate = LocalDateTime.parse(crew.createDate).minusDays(1L)
        val endDate = startDate.plusDays(DEFAULT_CREW_DURATION)
        val workoutDatas = workoutRepository
            .findAllByCurrentCrewAndUserIdAndCreateDateBetween(crew.crewId!!, user.uid!!, startDate, endDate)

        val response = workoutDatas
            ?.map{workout->getWorkoutByDay(workout)}
            ?.toList()

        return WorkoutGetDto.Response(response)
    }

    private fun getWorkoutByDay(workout: Workout): WorkoutGetDto.WorkoutByDay {
        val maxWorkoutData = workout.getMaxWorkoutPart()

        return WorkoutGetDto.WorkoutByDay(
            workoutDate = workout.createDate.format(dateTimeFormatter),
            totalTime = workout.getTotalTime(),
            averageHeartbeat = workout.getAverageHeartbeat(),
            totalCalories = workout.getTotalCalories(),
            maxWorkoutPart = maxWorkoutData.first.name,
            maxWorkoutPartTime = maxWorkoutData.second
        )
    }
}
