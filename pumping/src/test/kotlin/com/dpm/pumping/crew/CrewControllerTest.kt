package com.dpm.pumping.crew

import com.dpm.pumping.auth.application.JwtTokenProvider
import com.dpm.pumping.auth.dto.AccessTokenResponse
import com.dpm.pumping.crew.dto.CreateCrewRequest
import com.dpm.pumping.crew.dto.CrewResponse
import com.dpm.pumping.user.domain.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.Instant
import java.util.*

@WebMvcTest(CrewController::class)
@AutoConfigureRestDocs
class CrewControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper
) {
    @MockBean
    lateinit var crewService: CrewService

    @MockBean
    lateinit var jwtTokenProvider: JwtTokenProvider

    @MockBean
    lateinit var userRepository: UserRepository

    @Test
    fun `크루를 생성하는 API 테스트`() {
        // Request Body
        val requestBody = CreateCrewRequest(
            crewName = "크루1",
            goalCount = 7
        )

        val crewResponse = CrewResponse(
            crewId = "1",
            crewName = "크루1",
            goalCount = 7,
            code = "123456",
            participants = listOf("user1", "user2")
        )

        given(jwtTokenProvider.isValidAccessToken("token")).willReturn(true)

        `when`(crewService.createCrew(requestBody)).thenReturn(crewResponse)

        mockMvc.post("/crew/create") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestBody)
            header(HttpHeaders.AUTHORIZATION, "Bearer token" )
        }
            .andDo {
                handle(
                    document(
                        "crew-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                            fieldWithPath("crewName").description("크루 이름"),
                            fieldWithPath("goalCount").description("목표 횟수")
                        ),
                        responseFields(
                            fieldWithPath("crewId").description("크루 ID"),
                            fieldWithPath("crewName").description("크루 이름"),
                            fieldWithPath("goalCount").description("목표 횟수"),
                            fieldWithPath("code").description("크루 코드"),
                            fieldWithPath("participants").description("참여자 목록")
                        )
                    )
                )
            }
    }
}
