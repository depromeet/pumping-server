package com.dpm.pumping.home

import com.dpm.pumping.crew.Crew
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/home")
class HomeController(private val mongoTemplate: MongoTemplate) {
    private val logger: Logger = LoggerFactory.getLogger(HomeController::class.java)

    @PostMapping("/crew")
    fun getHomeData(@RequestBody request: HomeDataRequest): Any {
        val crewId = request.crewId
        val userId = request.userId

        val crewData = fetchCrewData(crewId)
        logger.info("[+] Fetched crewData: $crewData")
        if (crewData == null) {
            logger.error("[-] Failed to fetch crewData")
            return "해당 크루 정보를 찾을 수 없습니다."
        }


        val userData = fetchUserData(userId)
        logger.info("[+] Fetched userData: $userData")


        return HomeDataResponse(
            crewId = crewData.crewId,
            crewName = crewData.crewName,
            code = crewData.code,
            createDate = crewData.createDate,
            goalCount = crewData.goalCount,
            participants = crewData.participants,
            memberInfo = listOf(
                MemberInfo(
                    userId = "userId",
                    userName = "userName",
                    profileImage = "profileImage",
                    workoutCount = 1,
                    goalCount = 1,
                    workoutTime = 1
                )
            )
        )
    }

    private fun fetchCrewData(crewId: String): Crew? {
        // DB에서 해당 crewId에 맞는 데이터를 가져온다.
        val query = Query().addCriteria(Criteria.where("_id").`is`(crewId))
        return mongoTemplate.findOne(query, Crew::class.java, "crew")
    }

    private fun fetchUserData(userId: String): String {
        // DB에서 해당 userId에 맞는 데이터를 가져온다.
        return "userData"
    }

    private fun fetchWorkoutData(crewId: String, userId: String): String {
        return "workoutData"
    }
}

data class HomeDataRequest(
    val userId: String,
    val crewId: String
)

data class HomeDataResponse(
    val crewId: String?,
    val crewName: String?,
    val code: String?,
    val createDate: String?,
    val goalCount: Int?,
    val participants: List<String?>,
    val memberInfo: List<MemberInfo?>
)

data class MemberInfo(
    val userId: String?,
    val userName: String?,
    val profileImage: String?,
    val workoutCount: Int?,
    val goalCount: Int?,
    val workoutTime: Int?
)

@Document(collection = "crew")
data class Crew(
    @Id
    val crewId: String?,
    val crewName: String?,
    val code: String?,
    val createDate: String?,
    val goalCount: Int?,
    val participants: List<String?>
)