package com.dpm.pumping.crew

import com.dpm.pumping.auth.application.JwtTokenProvider
import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.crew.dto.CreateCrewRequest
import com.dpm.pumping.crew.dto.CrewResponse
import com.dpm.pumping.user.domain.CharacterType
import com.dpm.pumping.user.domain.Gender
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.user.domain.UserRepository
import com.dpm.pumping.workout.application.WorkoutService
import com.dpm.pumping.workout.dto.WorkoutCreateDto.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.util.*

@WebMvcTest(CrewController::class)
@AutoConfigureRestDocs
class CrewControllerTest (
    @Autowired val mockMvc: MockMvc,
    @Autowired val mapper: ObjectMapper,
) {
    @MockBean
    private lateinit var crewService: CrewService

    @MockBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @MockBean
    private lateinit var userRepository: UserRepository

    private val dummyUser = User(
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
        given(userRepository.findById(any())).willReturn(Optional.of(dummyUser))
    }
    @Test
    fun createCrew() {
        val request = CreateCrewRequest(
            crewName = "new Crew",
            goalCount = 5
        )

        val crewResponse = CrewResponse(
            crewId = "crewId",
            crewName = request.crewName,
            goalCount = request.goalCount,
            code = "123456",
            participants = listOf(dummyUser.uid)
        )

        given(crewService.create(request, dummyUser)).willReturn(crewResponse)


        mockMvc.post("/api/v1/crews"){
            header(HttpHeaders.AUTHORIZATION, "Bearer <Access Token>")
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
        }
        .andExpect { status{ isCreated()} }
            .andDo {
                handle(
                    document(
                        "create-crew",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                            fieldWithPath("crewName").type(JsonFieldType.STRING).description("크루 이름"),
                            fieldWithPath("goalCount").type(JsonFieldType.NUMBER).description("목표 횟수"),
                        ),
                        responseFields(
                            fieldWithPath("crewId").type(JsonFieldType.STRING).description("생성된 크루의 id"),
                            fieldWithPath("crewName").type(JsonFieldType.STRING).description("생성된 크루 이름"),
                            fieldWithPath("goalCount").type(JsonFieldType.NUMBER).description("목표 횟수"),
                            fieldWithPath("code").type(JsonFieldType.STRING).description("크루 참여를 위한 랜덤한 6자리 숫자"),
                            fieldWithPath("participants[]").type(JsonFieldType.ARRAY)
                                .description("크루에 속한 유저 id").optional(),
                        )
                    )
                )
            }
    }

    fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }
}
