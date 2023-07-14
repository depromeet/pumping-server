package com.dpm.pumping.user.presentation

import com.dpm.pumping.auth.application.AuthService
import com.dpm.pumping.auth.application.JwtTokenProvider
import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.auth.oauth2.OAuth2AppleClient
import com.dpm.pumping.crew.Crew
import com.dpm.pumping.support.Mocking.any
import com.dpm.pumping.user.application.UserService
import com.dpm.pumping.user.domain.CharacterType
import com.dpm.pumping.user.domain.Gender
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.user.domain.UserRepository
import com.dpm.pumping.user.dto.UserResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.*
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import java.util.*

@WebMvcTest(UserController::class)
@AutoConfigureRestDocs
class UserControllerTest(
    @Autowired val mockMvc: MockMvc,
) {

    @MockBean
    private lateinit var oAuth2AppleClient: OAuth2AppleClient

    @MockBean
    private lateinit var authService: AuthService


    @MockBean
    private lateinit var userService: UserService

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
        currentCrew = "111",
        characterType = CharacterType.A
    )

    @BeforeEach
    fun init() {
        given(jwtTokenProvider.isValidAccessToken(any())).willReturn(true)
        given(jwtTokenProvider.getAccessTokenPayload(any())).willReturn("1")
        given(userRepository.findById(any())).willReturn(Optional.of(dummyUser))
    }

    @Test
    fun getOne() {

//        val userResponse = UserResponse(
//            name = dummyUser.name,
//            characterType = dummyUser.characterType,
//            currentCrew = dummyUser.getCrewName()
//        )



        mockMvc.get("/api/v1/users") {
            header(HttpHeaders.AUTHORIZATION, "Bearer <Access Token>")
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect { status { isOk() } }
            .andDo {
                handle(
                    document(
                        "get-one",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                            fieldWithPath("name").type(JsonFieldType.STRING).description("유저 이름"),
                            fieldWithPath("characterType").type(JsonFieldType.STRING)
                                .description("유저 케릭터 타입"),
                            fieldWithPath("currentCrew").type(JsonFieldType.STRING)
                                .description("유저가 현재 속한 크루 이름"),
                        )
                    )
                )
            }
    }

    @Test
    fun delete(){

        mockMvc.delete("/api/v1/users") {
            header(HttpHeaders.AUTHORIZATION, "Bearer <Access Token>")
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect { status { isNoContent() } }
            .andDo {
                handle(
                    document(
                        "delete",
                        preprocessRequest(prettyPrint()),
                        requestHeaders(
                            headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer + 토큰")
                        ),
                    )
                )
            }
    }
}
