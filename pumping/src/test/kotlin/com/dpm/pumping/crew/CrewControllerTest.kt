package com.dpm.pumping.crew

import com.dpm.pumping.auth.application.JwtTokenProvider
import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.auth.dto.AccessTokenResponse
import com.dpm.pumping.crew.dto.CreateCrewRequest
import com.dpm.pumping.crew.dto.CrewResponse
import com.dpm.pumping.user.domain.Gender
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.user.domain.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
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

    private var testUser = User(
        uid = "user01",
        name = "name",
        gender = Gender.FEMALE,
        height = "160",
        weight = "50",
        platform = LoginPlatform(LoginType.APPLE, "oauth2Id"),
        currentCrew = null
    )
}
