import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.crew.Crew
import com.dpm.pumping.crew.CrewRepository
import com.dpm.pumping.crew.CrewService
import com.dpm.pumping.crew.dto.CreateCrewRequest
import com.dpm.pumping.user.domain.Gender
import com.dpm.pumping.user.domain.User
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CrewServiceTest {
    private val crewRepository = mock(CrewRepository::class.java)
    private val crewService = CrewService(crewRepository)
    private val logger = LoggerFactory.getLogger(CrewServiceTest::class.java)

    @Test
    fun testCreateCrew() {
        logger.info("Starting testCreateCrew...")

        val request = CreateCrewRequest("My Crew", 10)
        val user =  User(
            uid = "user01",
            name = "name",
            gender = Gender.FEMALE,
            height = "160",
            weight = "50",
            platform = LoginPlatform(LoginType.APPLE, "oauth2Id"),
            characterType = null,
            currentCrew = null
        )

        logger.info("Created request and user objects")

        val crewId = "crewId"
        val crew = Crew(
            crewId = crewId,
            crewName = request.crewName,
            code = "code",
            createDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
            goalCount = request.goalCount,
            participants = listOf(user.uid)
        )

        `when`(crewRepository.save(any(Crew::class.java))).thenReturn(crew)

        crewService.createCrew(request, user)

        logger.info("createCrew method called")

        // Verify that crewRepository.save() method was called
        verify(crewRepository).save(any())

        logger.info("Verified that crewRepository.save() was called")
    }
}
