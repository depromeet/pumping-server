package com.dpm.pumping.auth.presentation

import com.dpm.pumping.auth.application.AuthService
import com.dpm.pumping.auth.application.JwtTokenProvider
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.auth.dto.AccessTokenResponse
import com.dpm.pumping.auth.oauth2.OAuth2AppleClient
import com.dpm.pumping.auth.oauth2.dto.AppleLoginRequest
import com.dpm.pumping.auth.oauth2.dto.OAuth2LoginResponse
import com.dpm.pumping.auth.oauth2.dto.SignUpRequest
import com.dpm.pumping.user.domain.CharacterType
import com.dpm.pumping.user.domain.Gender
import com.dpm.pumping.user.domain.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime
import java.util.*


@WebMvcTest(AuthController::class)
@AutoConfigureRestDocs
class AuthControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
) {
    @MockBean
    private lateinit var oAuth2AppleClient: OAuth2AppleClient

    @MockBean
    private lateinit var authService: AuthService

    @MockBean
    private lateinit var jwtTokenProvider: JwtTokenProvider

    @MockBean
    private lateinit var userRepository: UserRepository

    @Test
    fun login() {
        val request = AppleLoginRequest("tokenId")
        val response = OAuth2LoginResponse(
            null,
            null,
            LoginType.APPLE,
            "OAauth_id"
        )

        given(oAuth2AppleClient.getAppleUserId(request)).willReturn("oauthID")
        given(authService.login("oauthID")).willReturn(response)

        mockMvc.post("/api/v1/oauth2/apple/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect { status { isOk() } }
            .andDo {
                handle(
                    document(
                        "oauth2-apple-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                            fieldWithPath("idToken").type(JsonFieldType.STRING).description("redirectUrl에서 받은 id_token")
                        ),
                        relaxedResponseFields(
                            fieldWithPath("accessToken").type(JsonFieldType.STRING)
                                .description("login했던 사용자면 accessToken, 최초 로그인 사용자면 null; 즉, null이면 회원가입 api 호출")
                                .optional(),
                            fieldWithPath("expiredTime").type(JsonFieldType.STRING)
                                .description("accessToken만료일, 최초 로그인이면 null").optional(),
                            fieldWithPath("loginType").type(JsonFieldType.STRING)
                                .description("loginType으로 애플 로그인이면 APPLE"),
                            fieldWithPath("oauth2Id").type(JsonFieldType.STRING)
                                .description("애플 로그인한 사용자의 애플 고유 id").optional(),
                        )
                    )
                )
            }
    }

    @Test
    fun signUp() {
        val request = SignUpRequest("name", Gender.MALE, "180.2", "90.4", CharacterType.A, LoginType.APPLE, "id")
        val response = AccessTokenResponse("accessToken", LocalDateTime.now().plusDays(5))
        given(authService.signUp(request)).willReturn(response)

        mockMvc.post("/api/v1/sign-up") {
            content = objectMapper.writeValueAsBytes(request)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
        }
            .andDo {
                handle(
                    document(
                        "signup",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                            fieldWithPath("name").type(JsonFieldType.STRING).description("유저 이름"),
                            fieldWithPath("gender").type(JsonFieldType.STRING).description("유저 성별"),
                            fieldWithPath("height").type(JsonFieldType.STRING).description("유저 키"),
                            fieldWithPath("weight").type(JsonFieldType.STRING).description("유저 몸무게"),
                            fieldWithPath("characterType").type(JsonFieldType.STRING).description("케릭터 타입"),
                            fieldWithPath("loginType").type(JsonFieldType.STRING)
                                .description("loginType으로 애플 로그인이면 APPLE"),
                            fieldWithPath("oauth2Id").type(JsonFieldType.STRING)
                                .description("애플 로그인한 사용자의 애플 고유 id").optional(),
                        ),
                        responseFields(
                            fieldWithPath("accessToken").type(JsonFieldType.STRING)
                                .description("login했던 사용자면 accessToken, 최초 로그인 사용자면 null; 즉, null이면 회원가입 api 호출")
                                .optional(),
                            fieldWithPath("expiredTime")
                                .description("accessToken만료일, 최초 로그인이면 null").optional(),
                        )
                    )
                )
            }
    }
}
