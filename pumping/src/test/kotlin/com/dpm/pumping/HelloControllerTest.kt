package com.dpm.pumping

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(HelloController::class)
@AutoConfigureRestDocs
class HelloControllerTest(
    @Autowired val mockMvc: MockMvc
){

    @Test
    fun test() {
        // when
        val result = mockMvc.perform(
            get("/hello")
                .accept(MediaType.APPLICATION_JSON)
        )

        // then
        result.andExpect(status().isOk)
            .andDo(document("hello",
                responseFields(
                    fieldWithPath("result").description("result value")
                )))
    }
}