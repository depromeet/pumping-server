package com.dpm.pumping.crew

import com.dpm.pumping.crew.dto.CreateCrewRequest
import com.dpm.pumping.crew.dto.CrewResponse
import com.dpm.pumping.user.domain.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class CrewService(@Autowired private val crewRepository: CrewRepository) {

    fun createCrew(request: CreateCrewRequest, user:User): CrewResponse {
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
        return CrewResponse(
            crewId = createdCrew.crewId,
            crewName = createdCrew.crewName,
            goalCount = createdCrew.goalCount,
            code = createdCrew.code,
            participants = createdCrew.participants
        )
    }

    // 크루 코드 생성 함수
    private fun generateCrewCode(): String {
        // 6자리의 랜덤한 숫자로 코드 생성
        val code = (1..999999).random().toString().padStart(6, '0')
        return code
    }

    // 크루 조회 함수 (by crewId)
    fun getCrew(crewId: String, user: User): CrewResponse {
        val crew = crewRepository.findById(crewId)
            .orElseThrow { RuntimeException("해당 크루 아이디로 찾을 수 없습니다.") }

        return CrewResponse(
            crewId = crew.crewId,
            crewName = crew.crewName,
            goalCount = crew.goalCount,
            code = crew.code,
            participants = crew.participants
        )
    }

    // 크루 조회 함수 (by code)
    fun getCrewByCode(code: String, user: User): CrewResponse {
        val crew = crewRepository.findByCode(code)
            ?: throw RuntimeException("해당 코드로 찾을 수 없습니다.")

        return CrewResponse(
            crewId = crew.crewId,
            crewName = crew.crewName,
            goalCount = crew.goalCount,
            code = crew.code,
            participants = crew.participants
        )
    }

    // 크루 참여 함수 (by code)
    fun joinCrew(code: String, user: User): CrewResponse {
        // 크루 조회
        val crew = crewRepository.findByCode(code)
            ?: throw RuntimeException("해당 코드로 찾을 수 없습니다.")

        // 크루 참여
//        val updatedCrew = crewRepository.save(
//            crew.copy(
//                participants = crew.participants + user.userId
//            )
//        )

        // 크루 정보 반환
        return CrewResponse(
            crewId = updatedCrew.crewId,
            crewName = updatedCrew.crewName,
            goalCount = updatedCrew.goalCount,
            code = updatedCrew.code,
            participants = updatedCrew.participants
        )
    }
}
