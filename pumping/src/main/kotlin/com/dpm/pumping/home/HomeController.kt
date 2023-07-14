package com.dpm.pumping.home

import com.dpm.pumping.auth.config.LoginUser
import com.dpm.pumping.auth.domain.LoginPlatform
import com.dpm.pumping.auth.domain.LoginType
import com.dpm.pumping.crew.Crew
import com.dpm.pumping.message.MessageService
import com.dpm.pumping.user.domain.CharacterType
import com.dpm.pumping.user.domain.Gender
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.workout.domain.entity.Workout
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/bypass")
class HomeController(
    private val mongoTemplate: MongoTemplate,
    private val messageService: MessageService
) {
    private val logger: Logger = LoggerFactory.getLogger(HomeController::class.java)

    @PostMapping("/check/uid")
    fun checkMyUserId(@LoginUser user: User): ResponseEntity<String> {
        val response = user.uid
        return response?.let { ResponseEntity(it, HttpStatus.OK) } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

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

        val memberInfoList = mutableListOf<MemberInfo>()
        crewData.participants.forEach { participantId ->
            val userData = participantId?.let { fetchUserData(it) }
            val workoutData = participantId?.let { fetchWorkoutData(crewId, it) }

            val workoutCount = workoutData?.timers?.size
            val workoutTotalTime = workoutData?.timers?.sumOf { it.time.toInt() }

            memberInfoList.add(
                MemberInfo(
                    userId = participantId,
                    userName = userData?.name,
                    profileImage = userData?.characterType,
                    workoutCount = workoutCount,
                    goalCount = crewData.goalCount,
                    workoutTime = workoutTotalTime
                )
            )
        }

        // 참여자 리스트 중에서 요청받은 userId를 가장 앞으로 옮긴다.
        val participantsList = crewData.participants.toMutableList()
        if (participantsList.contains(userId)) {
            participantsList.remove(userId)
        }
        participantsList.add(0, userId)

        // 참여자 리스트 중에서 요청받은 운동데이터를 가장 앞으로 옮긴다.
        val userIndex = memberInfoList.indexOfFirst { it.userId == userId }
        if (userIndex != -1) {
            val userWorkoutData = memberInfoList[userIndex]
            memberInfoList.removeAt(userIndex)
            memberInfoList.add(0, userWorkoutData)
        }
        val message = messageService.getMessage()

        return HomeDataResponse(
            crewId = crewData.crewId,
            crewName = crewData.crewName,
            code = crewData.code,
            createDate = crewData.createDate,
            goalCount = crewData.goalCount,
            message = message,
            participants = participantsList,
            memberInfo = memberInfoList
        )
    }

    private fun fetchCrewData(crewId: String): Crew? {
        // DB에서 해당 crewId에 맞는 데이터를 가져온다.
        val query = Query().addCriteria(Criteria.where("_id").`is`(crewId))
        return mongoTemplate.findOne(query, Crew::class.java, "crew")
    }

    private fun fetchUserData(userId: String): User? {
        // DB에서 해당 userId에 맞는 데이터를 가져온다.
        val query = Query().addCriteria(Criteria.where("_id").`is`(userId))
        return mongoTemplate.findOne(query, User::class.java, "user")
    }

    private fun fetchWorkoutData(crewId: String, userId: String): Workout? {
        val query = Query().addCriteria(
            Criteria.where("currentCrew").`is`(crewId).andOperator(
                Criteria.where("userId").`is`(userId)
            )
        )
        return mongoTemplate.findOne(query, Workout::class.java, "workout")
    }

    @PostMapping("/create/random/user")
    fun createRandomUser(): String {
        val randomId = (1..999999).random().toString()
        val randomName = "User-$randomId"
        val randomGender = Gender.MALE  // Set appropriate gender here
        val randomHeight = (100..200).random().toString()  // Set appropriate height here
        val randomWeight = (50..100).random().toString()  // Set appropriate weight here
        val randomPlatform =
            LoginPlatform(loginType = LoginType.FACEBOOK, oauth2Id = "test01")  // Set appropriate platform here
        val randomCharacterType = CharacterType.A  // Set appropriate characterType here
        val currentCrew = null

        val user = User(
            uid = randomId,
            name = randomName,
            gender = randomGender,
            height = randomHeight,
            weight = randomWeight,
            platform = randomPlatform,
            characterType = randomCharacterType,
            currentCrew = currentCrew
        )

        logger.info("[+] Created random user: $user")

        mongoTemplate.save(user)

        return user.uid.toString()
    }

    @PostMapping("/crew/join")
    fun joinCrew(@RequestBody request: HomeDataRequest): Any {
        val crewId = request.crewId
        val userId = request.userId

        val crewData = fetchCrewData(crewId)
        logger.info("[+] POST /bypass/crew/join ${request.toString()} crewData: $crewData")
        if (crewData == null) {
            logger.error("[-] Failed to fetch crewData")
            return "해당 크루 정보를 찾을 수 없습니다."
        }

        val userData = fetchUserData(userId)
        logger.info("[+] Fetched userData: $userData")
        if (userData == null) {
            logger.error("[-] Failed to fetch userData")
            return "해당 유저 정보를 찾을 수 없습니다."
        }

        val crewParticipants = crewData.participants.toMutableList()
        if (crewParticipants.contains(userId)) {
            logger.error("[-] User is already in the crew")
            return "이미 해당 크루에 참가하고 있습니다."
        }
        crewParticipants.add(userId.toString())
        crewData.participants = crewParticipants.toMutableList()

        mongoTemplate.save(crewData)

        return "크루 참가에 성공했습니다."
    }

    @PostMapping("/crew/switch")
    fun switchCrew(@RequestBody request: HomeDataRequest): Any {
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
        if (userData == null) {
            logger.error("[-] Failed to fetch userData")
            return "해당 유저 정보를 찾을 수 없습니다."
        }

        userData.currentCrew = crewId
        mongoTemplate.save(userData)

        logger.info("[+] POST /crew/switch ${request.toString()} userData: $userData")
        return "크루 변경에 성공했습니다."
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
        val message: String?,
        val participants: List<String?>,
        val memberInfo: List<MemberInfo?>
    )

    data class MemberInfo(
        val userId: String?,
        val userName: String?,
        val profileImage: CharacterType?,
        val workoutCount: Int?,
        val goalCount: Int?,
        val workoutTime: Int?
    )
}
