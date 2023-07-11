package com.dpm.pumping.user.application

import com.dpm.pumping.crew.CrewRepository
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.user.domain.UserRepository
import com.dpm.pumping.user.dto.DeleteUserEvent
import com.dpm.pumping.workout.repository.WorkoutRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService (
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher
){

    fun delete(user: User){
        val userId = user.uid!!
        eventPublisher.publishEvent(DeleteUserEvent(user))
        userRepository.deleteById(userId)
    }
}
