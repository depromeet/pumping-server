package com.dpm.pumping.crew

import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.crew.dto.CreateCrewRequest
import com.dpm.pumping.user.domain.Gender
import com.dpm.pumping.user.domain.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.MongoTemplate

@DataMongoTest
class CrewServiceIntegrationTest {

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Autowired
    lateinit var crewRepository: CrewRepository

    lateinit var crewService: CrewService

    @BeforeEach
    fun setup() {
        crewService = CrewService(crewRepository)
    }

    @Test
    fun testCreateCrew() {
        val request = CreateCrewRequest("My Crew", 10)
        val user = User(
            uid = "user01",
            name = "name",
            gender = Gender.FEMALE,
            height = "160",
            weight = "50",
            platform = LoginPlatform(LoginType.APPLE, "oauth2Id"),
            characterType = null,
            currentCrew = null
        )

        val response = crewService.createCrew(request, user)

        // Fetch the crew from database using mongoTemplate
        val crewFromDb = response.crewId?.let { mongoTemplate.findById(it, Crew::class.java) }

        assertNotNull(crewFromDb)
        assertEquals(crewFromDb!!.crewId, response.crewId)
    }

    // To Do - Fix this test
    // @AfterEach
    // fun cleanUp() {
    //     mongoTemplate.dropCollection(Crew::class.java)
    // }
}
