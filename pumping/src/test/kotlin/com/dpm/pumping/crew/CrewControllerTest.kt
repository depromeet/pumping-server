package com.dpm.pumping.crew

import com.dpm.pumping.auth.application.JwtTokenProvider
import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.crew.dto.CreateCrewRequest
import com.dpm.pumping.crew.dto.CrewResponse
import com.dpm.pumping.crew.dto.GetCrewResponse
import com.dpm.pumping.crew.dto.GetCrewsResponse
import com.dpm.pumping.support.Mocking.any
import com.dpm.pumping.user.domain.CharacterType
import com.dpm.pumping.user.domain.Gender
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.user.domain.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@WebMvcTest(CrewController::class)
@AutoConfigureRestDocs
@ActiveProfiles("test")
class CrewControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val mapper: ObjectMapper,
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


        mockMvc.post("/api/v1/crews") {
            header(HttpHeaders.AUTHORIZATION, "Bearer <Access Token>")
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
        }
            .andExpect { status { isCreated() } }
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
    fun joinCrew() {
        val code = "123456"

        val crewResponse = CrewResponse(
            crewId = "crewId",
            crewName = "new Crew",
            goalCount = 5,
            code = code,
            participants = listOf(dummyUser.uid)
        )

        given(crewService.joinCrew(code, dummyUser)).willReturn(crewResponse)


        mockMvc.post("/api/v1/crews/join/{code}", code) {
            header(HttpHeaders.AUTHORIZATION, "Bearer <Access Token>")
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect { status { isOk() } }
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
    fun leaveCrews() {
        val crewId = "crewId"
        val code = "123456"

        val response = CrewResponse(
            crewId = crewId,
            crewName = "leaved crew",
            goalCount = 0,
            code = code,
            participants = listOf(dummyUser.uid)
        )

        given(crewService.leaveCrew(any(), any())).willReturn(response)

        val result = mockMvc.perform(
            post("/api/v1/crews/leave/{crewId}", crewId)
                .header("Authorization", "Bearer <Access Token>")
                .accept(MediaType.APPLICATION_JSON)
        )

        result.andExpect { MockMvcResultMatchers.status().isOk }
            .andDo(
                document(
                    "leave-crew",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("crewId").description("나갈 크루 아이디")
                            .optional()
                    ),
                    responseFields(
                        fieldWithPath("crewId").type(JsonFieldType.STRING).description("크루 아이디"),
                        fieldWithPath("crewName").type(JsonFieldType.STRING).description("크루 이름"),
                        fieldWithPath("goalCount").type(JsonFieldType.NUMBER).description("목표 횟수"),
                        fieldWithPath("code").type(JsonFieldType.STRING).description("크루 코드"),
                        fieldWithPath("participants").type(JsonFieldType.ARRAY).description("업데이트된 크루 참여자 목록"),
                    )
                )
            )

    }

    @Test
    fun getCrews() {
        val code = "123456"

        val crewResponse = GetCrewsResponse(
            listOf(
                GetCrewResponse(
                    crewId = "crew01",
                    crewName = "name",
                    code = "123456",
                    createDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                )
            )
        )

        given(crewService.getCrews(dummyUser)).willReturn(crewResponse)


        mockMvc.get("/api/v1/crews") {
            header(HttpHeaders.AUTHORIZATION, "Bearer <Access Token>")
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect { status { isOk() } }
            .andDo {
                handle(
                    document(
                        "get-crews",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                            fieldWithPath("crews[].crewId").type(JsonFieldType.STRING).description("크루 아이디"),
                            fieldWithPath("crews[].crewName").type(JsonFieldType.STRING).description("크루 이름"),
                            fieldWithPath("crews[].code").type(JsonFieldType.STRING).description("크루 코드"),
                            fieldWithPath("crews[].createDate").type(JsonFieldType.STRING).description("크루 생성 날짜"),
                        )
                    )
                )
            }
    }


}
