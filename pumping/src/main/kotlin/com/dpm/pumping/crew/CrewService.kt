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

    // 크루 생성 후 참여
    fun createCrew(request: CreateCrewRequest, user: User): CrewResponse {
        val userId = user.uid

        // 크루 생성 및 저장
        val crew = Crew(
            crewId = null,
            crewName = request.crewName,
            code = generateCrewCode(), // 크루 코드 생성
            createDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
            goalCount = request.goalCount,
            participants = listOf(userId) // 크루 생성자는 참여자로 자동 등록
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

    // 크루 코드 생성
    private fun generateCrewCode(): String {
        // 6자리의 랜덤한 숫자로 코드 생성
        val code = (1..999999).random().toString().padStart(6, '0')
        return code
    }

    // 크루 참여 함수 (by code)
    fun joinCrew(code: String, user: User): CrewResponse {
        val userId = user.uid
        // 크루 조회
        val crew = crewRepository.findByCode(code)
            ?: throw RuntimeException("해당 코드로 찾을 수 없습니다.")

        // 참여자 추가
        val updatedParticipants = crew.participants.toMutableList()
        if (!updatedParticipants.contains(userId)) {
            updatedParticipants.add(userId.toString())
        }

        // 크루 업데이트
        val updatedCrew = crew.copy(participants = updatedParticipants)
        val savedCrew = crewRepository.save(updatedCrew)

        // 업데이트된 크루 정보 반환
        return CrewResponse(
            crewId = savedCrew.crewId,
            crewName = savedCrew.crewName,
            goalCount = savedCrew.goalCount,
            code = savedCrew.code,
            participants = savedCrew.participants
        )
    }
}
