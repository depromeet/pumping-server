package com.dpm.pumping.workout.application

import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.crew.Crew
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
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@SpringBootTest
class WorkoutServiceTest(
    @Autowired private val workoutService: WorkoutService,
    @Autowired private val workoutRepository: WorkoutRepository
) {

    @MockBean
    lateinit var userRepository: UserRepository

    @BeforeEach
    fun init(){
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
        val testUser = createUser(currentCrew = createCrew("crewO1"))

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
            workoutService.getWorkouts(testUser.uid!!)
        }.hasMessageContaining("아직 크루에 참여하지 않아 운동 기록이 존재하지 않습니다.")
    }

     @Test
    fun 각_크루별_가입일_이후_최대_7개_운동_데이터만_조회한다() {
        // given
        val crew1 = createCrew("crew01")
        val crew2 = createCrew("crew02")
        val testUser = createUser(currentCrew = crew1)
        val timer = createTimer(WorkoutPart.ARM)
        val crew1FirstDayWorkout = createWorkout(listOf(timer), "2023-06-22T10:00:00", crew1.crewId!!, testUser)
        val crew1SecondDayWorkout = createWorkout(listOf(timer), "2023-06-23T10:00:00", crew1.crewId!!, testUser)
        val crew2FirstDayWorkout = createWorkout(listOf(timer), "2023-06-23T10:00:00", crew2.crewId!!, testUser)

        workoutRepository.saveAll(listOf(crew1FirstDayWorkout, crew1SecondDayWorkout, crew2FirstDayWorkout))
        given(userRepository.findById(any())).willReturn(Optional.of(testUser))

        // when
        val response = workoutService.getWorkouts(testUser.uid!!)

        // then
        val result = response.workouts!!.toList()
        assertThat(result.size).isEqualTo(2)
        assertThat(result[0].workoutDate).isEqualTo(crew1FirstDayWorkout.createDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
        assertThat(result[1].workoutDate).isEqualTo(crew1SecondDayWorkout.createDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    }

    @Test
    fun 하루치_운동_데이터의_누적_값을_반환한다() {
        // given
        val crew = createCrew("crew01")
        val testUser = createUser(currentCrew = crew)
        val timer1 = createTimer(WorkoutPart.ARM)
        val timer2 = createTimer(WorkoutPart.HIP)
        val workout = createWorkout(listOf(timer1, timer2), "2023-06-22T10:00:00", crew.crewId!!, testUser)

        workoutRepository.save(workout)
        given(userRepository.findById(any())).willReturn(Optional.of(testUser))

        // when
        val result = workoutService.getWorkouts(testUser.uid!!)

        // then
        val values = result.workouts!!.toList()
        assertThat(values[0].totalTime).isEqualTo(120)
        assertThat(values[0].averageHeartbeat).isEqualTo(80)
        assertThat(values[0].totalCalories).isEqualTo(200)
    }

    @Test
    fun 하루_동안_가장_많이_운동한_부위를_반환한다() {
        // given
        val crew = createCrew("crew01")
        val testUser = createUser(currentCrew = crew)
        val timer1 = createTimer(WorkoutPart.ARM)
        val timer2 = createTimer(WorkoutPart.CHEST)
        val timer3 = createTimer(WorkoutPart.LEG)
        val workout = createWorkout(listOf(timer1, timer2, timer3), "2023-06-22T10:00:00", crew.crewId!!, testUser)

        workoutRepository.save(workout)
        given(userRepository.findById(any())).willReturn(Optional.of(testUser))

        // when
        val result = workoutService.getWorkouts(testUser.uid!!)

        // then
        val values = result.workouts!!.toList()
        assertThat(values[0].maxWorkoutPart).isEqualTo(WorkoutCategory.UP.name)
        assertThat(values[0].maxWorkoutPartTime).isEqualTo(120)
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

    private fun createWorkout(timers: List<Timer>, createDate: String, crewId: String, user: User): Workout {
        return Workout(
            userId = user.uid!!,
            timers = timers,
            createDate = LocalDateTime.parse(createDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            currentCrew = crewId
        )
    }

    private fun createCrew(id: String): Crew {
        return Crew(
            crewId = id,
            crewName = "name",
            code = "0248291",
            createDate = "2023-06-22T10:00:00",
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

    fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }
}

