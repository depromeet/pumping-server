package com.dpm.pumping.user.application

import com.dpm.pumping.crew.CrewRepository
import com.dpm.pumping.crew.CrewService
import com.dpm.pumping.user.dto.DeleteUserEvent
import com.dpm.pumping.workout.repository.WorkoutRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

@Component
class UserDeleteEventHandler(
    private val workoutRepository: WorkoutRepository,
    private val crewRepository: CrewRepository
) {

    @Async("deleteUserThreadPoolTaskExecutor")
    @TransactionalEventListener
    fun deleteWorkOutAndCrewParticipants(event: DeleteUserEvent) {
        val userId = event.user.uid!!
        val crews = crewRepository.findAllCrewsByParticipantsContains(userId)
        crews.stream()
            .forEach {
                val updatedParticipants = it.participants.toMutableList()
                if (updatedParticipants.contains(userId)) {
                    updatedParticipants.remove(userId)
                }
                // 크루 업데이트
                val updatedCrew = it.copy(participants = updatedParticipants)
                crewRepository.save(updatedCrew)
            }
        workoutRepository.deleteAllByUserId(event.user.uid!!)
    }
}
