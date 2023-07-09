package com.dpm.pumping.crew

import com.dpm.pumping.crew.dto.CreateCrewRequest
import com.dpm.pumping.crew.dto.CrewResponse
import com.dpm.pumping.crew.dto.GetCrewResponse
import com.dpm.pumping.crew.dto.GetCrewsResponse
import com.dpm.pumping.user.domain.User
import org.springframework.stereotype.Service

@Service
class CrewService(
    private val crewRepository: CrewRepository
) {

    fun create(request: CreateCrewRequest, user: User): CrewResponse {
        val crew = Crew.create(name = request.crewName, goalCount = request.goalCount, userId = user.uid!!)
        val createdCrew = crewRepository.save(crew)

        return CrewResponse(
            crewId = createdCrew.crewId,
            crewName = createdCrew.crewName,
            goalCount = createdCrew.goalCount,
            code = createdCrew.code,
            participants = createdCrew.participants
        )
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

    // 크루 탈퇴 함수
    fun leaveCrew(crewId: String, user: User): CrewResponse {
        val userId = user.uid
        // 크루 조회
        val crew = crewRepository.findByCrewId(crewId)
            ?: throw RuntimeException("해당 크루를 찾을 수 없습니다.")

        // 참여자 제거
        val updatedParticipants = crew.participants.toMutableList()
        if (updatedParticipants.contains(userId)) {
            updatedParticipants.remove(userId)
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

    // 내가 참여한 모든 크루 조회 (by userId)
    // 가장 최신 순 정렬 후 반환
    fun getCrews(user: User): GetCrewsResponse {
        val userId = user.uid
        val crews = crewRepository.findAllCrewsByParticipantsContains(userId.toString())
        val sortedCrews = crews.sortedByDescending { it.createDate }
        val result = sortedCrews.map { crew: Crew ->
            GetCrewResponse(
                crewId = crew.crewId,
                crewName = crew.crewName,
                createDate = crew.createDate
            )
        }

        return GetCrewsResponse(result)
    }
}
