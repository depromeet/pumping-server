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
import com.dpm.pumping.workout.dto.WorkoutCreateDto.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.*
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
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
                        requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer + 토큰")
                        ),
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


    @Test
    fun joinCrew(){
        val code = "123456"

        val crewResponse = CrewResponse(
            crewId = "crewId",
            crewName = "new Crew",
            goalCount = 5,
            code = code,
            participants = listOf(dummyUser.uid)
        )

        given(crewService.joinCrew(code, dummyUser)).willReturn(crewResponse)


        mockMvc.post("/api/v1/crews/join/{code}", code){
            header(HttpHeaders.AUTHORIZATION, "Bearer <Access Token>")
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect { status{ isOk() } }
            .andDo {
                handle(
                    document(
                        "join-crew",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
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

    @Test
    fun getCrews(){
        val code = "123456"

        val crewResponse = CrewResponse(
            crewId = "crewId",
            crewName = "new Crew",
            goalCount = 5,
            code = code,
            participants = listOf(dummyUser.uid)
        )

        given(crewService.getCrews(dummyUser)).willReturn(any())


        mockMvc.get("/api/v1/crews"){
            header(HttpHeaders.AUTHORIZATION, "Bearer <Access Token>")
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect { status{ isOk() } }
            .andDo {
                handle(
                    document(
                        "get-crews",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
//                        responseFields(
//                            //TODO: dto 변환 후, 적용
//                        )
                    )
                )
            }
    }

    fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }
}
