package com.dpm.pumping.auth.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class TokenParserTest {

    @Test
    fun `Bearer을 제외한 토큰을 추출한다`() {
        val payload = "testtesttest"
        val token = "Bearer $payload"
        val extract = TokenParser.extract(token)

        assertThat(extract).isEqualTo(payload)
    }
}
