package com.dpm.pumping.workout.application

import com.dpm.pumping.user.domain.User
import com.dpm.pumping.user.domain.UserRepository
import com.dpm.pumping.workout.application.WorkoutStorage.WorkoutByDay
import com.dpm.pumping.workout.domain.entity.Workout
import com.dpm.pumping.workout.dto.WorkoutCreateDto
import com.dpm.pumping.workout.dto.WorkoutGetDto
import com.dpm.pumping.workout.repository.WorkoutRepository
import com.dpm.pumping.workout.util.CalenderUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class WorkoutService(
    private val workoutRepository: WorkoutRepository,
    private val userRepository: UserRepository
){

    @Transactional
    fun createWorkout(
        request: WorkoutCreateDto.Request, user: User
    ): WorkoutCreateDto.Response {
        val crew = user.currentCrew
            ?: throw IllegalArgumentException("아직 크루에 참여하지 않아 운동 기록을 저장할 수 없습니다.")

        val workout = Workout.of(user.uid!!, crew.crewId!!, request.timers)
        val created = workoutRepository.save(workout)
        return WorkoutCreateDto.Response(created.workoutId!!)
    }

    fun getWorkouts(userId: String?, loginUser: User): WorkoutGetDto.Response {
        val user = getUser(userId, loginUser)
        val crew = user.currentCrew
            ?: throw IllegalArgumentException("아직 크루에 참여하지 않아 운동 기록이 존재하지 않습니다.")

        val startDate = crew.getStartDate()
        val endDate = startDate.plusDays(WorkoutStorage.DEFAULT_SIZE)
        val workoutDatas = workoutRepository
            .findAllByCurrentCrewAndUserIdAndCreateDateBetween(crew.crewId!!, user.uid!!, startDate.minusDays(1), endDate)

        val storage = WorkoutStorage(startDate.toLocalDate())
        workoutDatas?.forEach {
            storage.save(it)
        }

        val result = getWorkoutDto(storage.get())
        return WorkoutGetDto.Response(result)
    }

    private fun getUser(userId: String?, loginUser: User): User {
        return if (userId === null) {
            loginUser
        } else {
            userRepository.findById(userId)
                .orElseThrow { throw IllegalArgumentException("${userId}에 해당하는 유저를 찾을 수 없습니다.") }
        }
    }

    private fun getWorkoutDto(storage: Map<LocalDate, WorkoutByDay?>): List<WorkoutGetDto.WorkoutResponse> {
        return storage.map { (key, value) ->
            val day = CalenderUtils.getDayOfWeek(key)
            WorkoutGetDto.WorkoutResponse(day.toString(), value)
        }
    }
}
