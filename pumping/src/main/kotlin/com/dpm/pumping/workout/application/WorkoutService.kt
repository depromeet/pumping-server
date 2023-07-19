package com.dpm.pumping.workout.application

import com.dpm.pumping.crew.Crew
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.user.domain.UserRepository
import com.dpm.pumping.workout.domain.entity.Workout
import com.dpm.pumping.workout.dto.WorkoutCreateDto
import com.dpm.pumping.workout.dto.WorkoutGetDto
import com.dpm.pumping.workout.repository.WorkoutRepository
import com.dpm.pumping.workout.util.CalenderUtils
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class WorkoutService(
    private val workoutRepository: WorkoutRepository,
    private val userRepository: UserRepository,
    private val mongoTemplate: MongoTemplate,
) {
    private fun fetchCrewData(crewId: String): Crew? {
        // DB에서 해당 crewId에 맞는 데이터를 가져온다.
        val query = Query().addCriteria(Criteria.where("_id").`is`(crewId))
        return mongoTemplate.findOne(query, Crew::class.java, "crew")
    }

    @Transactional
    fun createWorkout(
        request: WorkoutCreateDto.Request, user: User
    ): WorkoutCreateDto.Response {
        val crewId = user.currentCrew
            ?: throw IllegalArgumentException("아직 크루에 참여하지 않아 운동 기록을 저장할 수 없습니다.")

        val crew = fetchCrewData(crewId) ?: throw IllegalArgumentException("크루 정보를 찾을 수 없습니다.")
        val workout = Workout.of(user.uid!!, crew.crewId!!, request.timers)
        val created = workoutRepository.save(workout)
        return WorkoutCreateDto.Response(created.workoutId!!)
    }

    fun getWorkouts(userId: String?, loginUser: User): WorkoutGetDto.Response {
        val user = getUser(userId, loginUser)
        val crewId = user.currentCrew
            ?: throw IllegalArgumentException("아직 크루에 참여하지 않아 운동 기록이 존재하지 않습니다.")

        val crew = fetchCrewData(crewId) ?: throw IllegalArgumentException("크루 정보를 찾을 수 없습니다.")
        val startDate = crew.getStartDate()
        val endDate = startDate.plusDays(WorkoutStorage.DEFAULT_SIZE)
        val workoutDatas = workoutRepository
            .findAllByCurrentCrewAndUserIdAndCreateDateBetween(crew.crewId!!, user.uid!!, startDate.minusDays(1), endDate)

        val storage = WorkoutStorage(startDate.toLocalDate())
        workoutDatas?.forEach {
            storage.save(it)
        }

        val workoutResponse = mapWorkoutResponseDto(storage.get())
        return WorkoutGetDto.Response(workoutResponse)
    }

    private fun getUser(userId: String?, loginUser: User): User {
        return if (userId === null) {
            loginUser
        } else {
            userRepository.findById(userId)
                .orElseThrow { throw IllegalArgumentException("${userId}에 해당하는 유저를 찾을 수 없습니다.") }
        }
    }

    private fun mapWorkoutResponseDto(storage: Map<WorkoutStorage.Date, Workout?>): List<WorkoutGetDto.WorkoutResponse> {
        return storage.map { (key, value) ->
            WorkoutGetDto.WorkoutResponse(
                dayOfWeek =  CalenderUtils.getDayOfWeek(key.workoutDate).toString(),
                workout = WorkoutGetDto.DailyWorkout(
                    workoutDate = key.workoutDayCount.toString(),
                    totalTime = value?.let { value.getTotalTime() } ?: 0,
                    averageHeartbeat = value?.let { value.getAverageHeartbeat() } ?: 0,
                    totalCalories = value?.let { value.getTotalCalories() } ?: 0,
                    maxWorkoutCategory = value?.let {  value.getMaxWorkoutPart().first.name },
                    maxWorkoutCategoryTime = value?.let { value.getMaxWorkoutPart().second } ?: 0
                )
            )
        }
    }
}
