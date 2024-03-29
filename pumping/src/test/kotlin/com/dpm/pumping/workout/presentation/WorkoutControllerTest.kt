package com.dpm.pumping.workout.presentation

import com.dpm.pumping.auth.application.JwtTokenProvider
import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.support.Mocking.any
import com.dpm.pumping.user.domain.CharacterType
import com.dpm.pumping.user.domain.Gender
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.user.domain.UserRepository
import com.dpm.pumping.workout.application.WorkoutService
import com.dpm.pumping.workout.application.WorkoutStorage
import com.dpm.pumping.workout.domain.WorkoutCategory
import com.dpm.pumping.workout.domain.WorkoutPart
import com.dpm.pumping.workout.dto.WorkoutCreateDto.*
import com.dpm.pumping.workout.dto.WorkoutGetDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*


@WebMvcTest(WorkoutController::class)
@AutoConfigureRestDocs
@ActiveProfiles("test")
class WorkoutControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val mapper: ObjectMapper
) {

    @MockBean
    lateinit var workoutService: WorkoutService

    @MockBean
    lateinit var jwtTokenProvider: JwtTokenProvider

    @MockBean
    lateinit var userRepository: UserRepository

    private var testUser = User(
        uid = "user01",
        name = "name",
        gender = Gender.FEMALE,
        height = "160",
        weight = "50",
        platform = LoginPlatform(LoginType.APPLE, "oauth2Id"),
        currentCrew = null,
        characterType = CharacterType.A
    )

    @BeforeEach
    fun init() {
        given(jwtTokenProvider.isValidAccessToken(any())).willReturn(true)
        given(jwtTokenProvider.getAccessTokenPayload(any())).willReturn("1")
        given(userRepository.findById(any())).willReturn(Optional.of(testUser))
    }

    @Test
    fun createWorkout() {
        val request = Request(
            timers = listOf(
                TimerDto(
                    time = 600,
                    heartbeat = 100,
                    calories = 500,
                    workoutPart = "가슴",
                    workoutSets = listOf(
                        WorkoutSetDto(machine = "체스트 프레스", kg = 100, sets = 3)
                    )
                )
            )
        )

        val response = Response("workout01")

        given(workoutService.createWorkout(request, testUser)).willReturn(response)

        val result = mockMvc.perform(
            post("/api/v1/workout")
                .header("Authorization", "Bearer <Access Token>")
                .content(mapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

        result.andExpect { status().isOk }
            .andDo(
                document(
                    "create-workout",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("timers[].time").type(JsonFieldType.NUMBER).description("운동 시간"),
                        fieldWithPath("timers[].heartbeat").type(JsonFieldType.NUMBER).description("심박수"),
                        fieldWithPath("timers[].calories").type(JsonFieldType.NUMBER).description("소모 칼로리"),
                        fieldWithPath("timers[].workoutPart").type(JsonFieldType.STRING)
                            .description("운동 부위 : AEROBIC, SHOULDER, CHEST, ARM, BACK, HIP, LEG"),
                        fieldWithPath("timers[].workoutSets[].machine").type(JsonFieldType.STRING)
                            .description("운동 기구 : SP, SLL, DSP, AHP, CP, MP, MF, BP, SIB, DK, BK, KK, AM, MK, LD, LOP, SD, BR, HA, HS, STD, LE, LGP, LK, DL, SQ")
                            .optional(),
                        fieldWithPath("timers[].workoutSets[].kg").type(JsonFieldType.NUMBER).description("무게")
                            .optional(),
                        fieldWithPath("timers[].workoutSets[].sets").type(JsonFieldType.NUMBER).description("세트")
                            .optional(),
                    ),
                    responseFields(
                        fieldWithPath("uid").type(JsonFieldType.STRING).description("생성된 운동 ID")
                    )
                )
            )
    }

    @Test
    fun getWorkouts() {
        val response = WorkoutGetDto.Response(
            listOf(
                WorkoutGetDto.WorkoutResponse(
                    dayOfWeek = "1",
                    workout = WorkoutGetDto.DailyWorkout(
                        workoutDate = "3",
                        totalTime = 60,
                        averageHeartbeat = 120,
                        totalCalories = 500,
                        maxWorkoutCategory = WorkoutCategory.UP.name,
                        maxWorkoutCategoryTime = 100
                    ),
                )
            )
        )

        given(workoutService.getWorkouts(any(), any())).willReturn(response)

        val result = mockMvc.perform(
            get("/api/v1/workout/{userId}", testUser.uid)
                .header("Authorization", "Bearer <Access Token>")
                .accept(MediaType.APPLICATION_JSON)
        )

        result.andExpect { status().isOk }
            .andDo(
                document(
                    "get-workouts",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("userId").description("친구 아이디: 본인 운동 데이터 조회에서는 필요 X").optional()
                    ),
                    responseFields(
                        fieldWithPath("workouts[].dayOfWeek").type(JsonFieldType.STRING)
                            .description("운동 요일 (월부터 1, 일요일은 7)"),
                        fieldWithPath("workouts[].workout.workoutDate").type(JsonFieldType.STRING).description("운동 일차"),
                        fieldWithPath("workouts[].workout.totalTime").type(JsonFieldType.NUMBER)
                            .description("하루 동안 누적 운동 시간"),
                        fieldWithPath("workouts[].workout.averageHeartbeat").type(JsonFieldType.NUMBER)
                            .description("하루 동안 평균 심박수"),
                        fieldWithPath("workouts[].workout.totalCalories").type(JsonFieldType.NUMBER)
                            .description("하루 동안 누적 소모 칼로리"),
                        fieldWithPath("workouts[].workout.maxWorkoutCategory").type(JsonFieldType.STRING)
                            .description("하루 동안 최대 운동한 부위: WHOLE(전신) / UP(상체) / DOWN(하체) "),
                        fieldWithPath("workouts[].workout.maxWorkoutCategoryTime").type(JsonFieldType.NUMBER)
                            .description("하루 동안 최대 운동한 부위 시간")
                    )
                )
            )
    }
}
