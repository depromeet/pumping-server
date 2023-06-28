package com.dpm.pumping.crew

import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.crew.dto.CreateCrewRequest
import com.dpm.pumping.crew.dto.CrewResponse
import com.dpm.pumping.user.domain.Gender
import com.dpm.pumping.user.domain.User
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.MongoTemplate

@DataMongoTest
class CrewServiceIntegrationTest {
    private val logger: Logger = LoggerFactory.getLogger(CrewServiceIntegrationTest::class.java)

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Autowired
    lateinit var crewRepository: CrewRepository

    lateinit var crewService: CrewService

    @BeforeEach
    fun setup() {
        crewService = CrewService(crewRepository)
    }

    private var tempResponse: CrewResponse? = null

    @Test
    fun testCreateCrew() {
        val request = CreateCrewRequest("0번크루", 10)
        val user = User(
            uid = "user01",
            name = "name1",
            gender = Gender.FEMALE,
            height = "160",
            weight = "50",
            platform = LoginPlatform(LoginType.APPLE, "oauth2Id"),
            characterType = null,
            currentCrew = null
        )

        val response = crewService.createCrew(request, user)
        logger.info("Create Crew Response: $response")

        // Fetch the crew from database using mongoTemplate
        val crewFromDb = response.crewId?.let { mongoTemplate.findById(it, Crew::class.java) }

        assertNotNull(crewFromDb)
        assertEquals(crewFromDb!!.crewId, response.crewId)

        // Store the response in a local variable
        val tempResponse = response
    }

    @Test
    fun testJoinCrew() {
        val request = CreateCrewRequest("1번크루", 10)
        val user2 = User(
            uid = "user02",
            name = "name2",
            gender = Gender.FEMALE,
            height = "160",
            weight = "50",
            platform = LoginPlatform(LoginType.APPLE, "oauth2Id"),
            characterType = null,
            currentCrew = null
        )
        val user3 = User(
            uid = "user03",
            name = "name4",
            gender = Gender.FEMALE,
            height = "160",
            weight = "50",
            platform = LoginPlatform(LoginType.APPLE, "oauth2Id"),
            characterType = null,
            currentCrew = null
        )

        // Create a crew
        val response = crewService.createCrew(request, user2)
        logger.info("Create Crew Response: $response")

        // Join the crew
        val joinResponse = response.code?.let { crewService.joinCrew(it, user2) }
        logger.info("Join Crew Response: $joinResponse")
    }

    @Test
    fun testGetCrewsByUserId(){
        val user = User(
            uid = "user01",
            name = "name1",
            gender = Gender.FEMALE,
            height = "160",
            weight = "50",
            platform = LoginPlatform(LoginType.APPLE, "oauth2Id"),
            characterType = null,
            currentCrew = null
        )
        crewService.createCrew(CreateCrewRequest("1번크루", 11), user)
        crewService.createCrew(CreateCrewRequest("2번크루", 22), user)
        crewService.createCrew(CreateCrewRequest("3번크루", 33), user)
        val response = crewService.getCrews(user)
        logger.info("Get Crews Response: $response")
    }


    // To Do - Fix this test
    // @AfterEach
    // fun cleanUp() {
    //     mongoTemplate.dropCollection(Crew::class.java)
    // }
}
