package com.dpm.pumping

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest
@AutoConfigureMockMvc
class PumpingApplicationTests {

	@Autowired
	private lateinit var mockMvc: MockMvc

	@Test
	fun contextLoads() {
	}
}
