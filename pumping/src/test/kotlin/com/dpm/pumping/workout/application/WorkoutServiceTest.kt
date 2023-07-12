package com.dpm.pumping.workout.application

import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.crew.Crew
import com.dpm.pumping.support.Mocking.any
import com.dpm.pumping.user.domain.CharacterType
import com.dpm.pumping.user.domain.Gender
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.user.domain.UserRepository
import com.dpm.pumping.workout.domain.WorkoutCategory
import com.dpm.pumping.workout.domain.WorkoutPart
import com.dpm.pumping.workout.domain.entity.Timer
import com.dpm.pumping.workout.domain.entity.Workout
import com.dpm.pumping.workout.dto.WorkoutCreateDto
import com.dpm.pumping.workout.repository.WorkoutRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.*
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
class WorkoutServiceTest @Autowired constructor(
    private val workoutService: WorkoutService,
    private val workoutRepository: WorkoutRepository
) {

    @MockBean
    lateinit var userRepository: UserRepository

    private val currentTime = LocalDateTime.now()

    @BeforeEach
    fun init() {
        workoutRepository.deleteAll()
    }

    @Test
    fun 운동_기록에_성공한다() {
        val workoutSetDto = WorkoutCreateDto.WorkoutSetDto(
            machine = "CP",
            kg = 100,
            sets = 3
        )

        val timerDto = WorkoutCreateDto.TimerDto(
            time = 0,
            heartbeat = 0,
            calories = 0,
            workoutPart = "CHEST",
            workoutSets = listOf(workoutSetDto)
        )

        val request = WorkoutCreateDto.Request(
            timers = listOf(timerDto)
        )
        val testUser = createUser(currentCrew = createCrew("crewO1", currentTime))

        val response = workoutService.createWorkout(request, testUser)

        assertThat(response.uid).isNotNull
    }

    @Test
    fun 크루에_참여하지_않았다면_예외가_발생한다() {
        // given
        val testUser = createUser(currentCrew = null)
        given(userRepository.findById(any())).willReturn(Optional.of(testUser))

        // when + then
        assertThatThrownBy {
            workoutService.getWorkouts(null, testUser)
        }.hasMessageContaining("아직 크루에 참여하지 않아 운동 기록이 존재하지 않습니다.")
    }

    @Test
    fun 각_크루별_가입일_이후_최대_7개_운동_데이터만_조회한다() {
        // given
        val crew1 = createCrew("crew01", currentTime)
        val crew2StartDate = currentTime.plusDays(1)
        val crew2 = createCrew("crew02", crew2StartDate)
        val testUser = createUser(currentCrew = crew1)
        val timer = createTimer(WorkoutPart.ARM)

        for (i in 2..8) {
            val createdDate = currentTime.plusDays(i.toLong())
            val entity = createWorkout(listOf(timer), createdDate, crew1.crewId!!, testUser)
            workoutRepository.save(entity)
        }

        val crew2FirstDayWorkout = createWorkout(listOf(timer), crew2StartDate, crew2.crewId!!, testUser)
        workoutRepository.save(crew2FirstDayWorkout)

        given(userRepository.findById(any())).willReturn(Optional.of(testUser))

        // when
        val response = workoutService.getWorkouts(null, testUser)

        // then
        val result = response.workouts
        assertThat(result.size).isEqualTo(7)
        assertThat(result[3].workout!!.workoutDate).isEqualTo("4")
        assertThat(result[4].workout!!.workoutDate).isEqualTo("5")
        assertThat(result[5].workout!!.workoutDate).isEqualTo("6")
        assertThat(result[6].workout!!.workoutDate).isEqualTo("7")
    }

    @Test
    fun 하루치_운동_데이터의_누적_값을_반환한다() {
        // given
        val crew = createCrew("crew01", currentTime)
        val testUser = createUser(currentCrew = crew)
        val timer1 = createTimer(WorkoutPart.ARM)
        val timer2 = createTimer(WorkoutPart.HIP)
        val workout = createWorkout(listOf(timer1, timer2), currentTime, crew.crewId!!, testUser)

        workoutRepository.save(workout)
        given(userRepository.findById(any())).willReturn(Optional.of(testUser))

        // when
        val result = workoutService.getWorkouts(null, testUser)

        // then
        val values = result.workouts
        assertThat(values[0].workout!!.totalTime).isEqualTo(120)
        assertThat(values[0].workout!!.averageHeartbeat).isEqualTo(80)
        assertThat(values[0].workout!!.totalCalories).isEqualTo(200)
    }

    @Test
    fun 하루_동안_가장_많이_운동한_부위를_반환한다() {
        // given
        val crew = createCrew("crew01", currentTime)
        val testUser = createUser(currentCrew = crew)
        val timer1 = createTimer(WorkoutPart.ARM)
        val timer2 = createTimer(WorkoutPart.CHEST)
        val timer3 = createTimer(WorkoutPart.LEG)
        val workout = createWorkout(listOf(timer1, timer2, timer3), currentTime, crew.crewId!!, testUser)

        workoutRepository.save(workout)
        given(userRepository.findById(any())).willReturn(Optional.of(testUser))

        // when
        val result = workoutService.getWorkouts(null, testUser)

        // then
        val values = result.workouts
        assertThat(values[0].workout!!.maxWorkoutCategory).isEqualTo(WorkoutCategory.UP.name)
        assertThat(values[0].workout!!.maxWorkoutCategoryTime).isEqualTo(120)
    }

    @Test
    fun `2주차_데이터를_반환한다`() {
        val startDate = currentTime.minusDays(10)
        val crew1 = createCrew("crew01", startDate)
        val testUser = createUser(currentCrew = crew1)
        val timer = createTimer(WorkoutPart.ARM)

        for (i in 2..6) {
            val createdDate = startDate.plusDays(i.toLong())
            val entity = createWorkout(listOf(timer), createdDate, crew1.crewId!!, testUser)
            workoutRepository.save(entity)
        }

        for (i in 10..12) {
            val createdDate = startDate.plusDays(i.toLong())
            val entity = createWorkout(listOf(timer), createdDate, crew1.crewId!!, testUser)
            workoutRepository.save(entity)
        }

        given(userRepository.findById(any())).willReturn(Optional.of(testUser))

        // when
        val response = workoutService.getWorkouts(null, testUser)

        // then
        val result = response.workouts
        assertThat(result.size).isEqualTo(7)
        assertThat(result[3].workout!!.workoutDate).isEqualTo("4")
        assertThat(result[4].workout!!.workoutDate).isEqualTo("5")
        assertThat(result[5].workout!!.workoutDate).isEqualTo("6")
    }

    private fun createTimer(workoutPart: WorkoutPart): Timer {
        return Timer(
            timerId = "timer01",
            workoutPart = workoutPart.name,
            time = "60",
            calories = "100",
            heartbeat = "80"
        )
    }

    private fun createWorkout(timers: List<Timer>, createDate: LocalDateTime, crewId: String, user: User): Workout {
        return Workout(
            userId = user.uid!!,
            timers = timers,
            createDate = createDate,
            currentCrew = crewId
        )
    }

    private fun createCrew(id: String, createdAt: LocalDateTime): Crew {
        return Crew(
            crewId = id,
            crewName = "name",
            code = "0248291",
            createDate = createdAt.format(ISO_LOCAL_DATE_TIME),
            goalCount = 5,
            participants = listOf("user01")
        )
    }

    private fun createUser(currentCrew: Crew?): User {
        return User(
            uid = "user01",
            name = "name",
            gender = Gender.FEMALE,
            height = "160",
            weight = "50",
            platform = LoginPlatform(LoginType.APPLE, "oauth2Id"),
            currentCrew = currentCrew,
            characterType = CharacterType.A
        )
    }
}

