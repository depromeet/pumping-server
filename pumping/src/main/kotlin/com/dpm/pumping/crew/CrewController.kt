package com.dpm.pumping.crew

import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/crew")
@Api(tags = ["크루 관련 API"])
class CrewController {
    @Autowired
    lateinit var crewRepository: CrewRepository

    // 크루 생성 API
    @PostMapping("/create")
    fun createCrew(@RequestBody request: CreateCrewRequest): ResponseEntity<CrewResponse> {
        // 크루 생성 및 저장
        val crew = Crew(
            crewId = null,
            crewName = request.crewName,
            code = generateCrewCode(), // 크루 코드 생성
            createDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
            goalCount = request.goalCount,
            participants = emptyList()
        )
        val createdCrew = crewRepository.save(crew)

        // 생성된 크루 정보 반환
        val response = CrewResponse(
            crewId = createdCrew.crewId,
            crewName = createdCrew.crewName,
            goalCount = createdCrew.goalCount,
            code = createdCrew.code
        )
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    // 크루 코드 생성 함수
    private fun generateCrewCode(): String {
        // 6자리의 랜덤한 숫자로 코드 생성
        val code = (1..999999).random().toString().padStart(6, '0')
        return code
    }
}

data class CreateCrewRequest(
    val crewName: String,
    val goalCount: Int
)

data class CrewResponse(
    val crewId: String?,
    val crewName: String?,
    val goalCount: Int?,
    val code: String?
)
