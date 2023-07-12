package com.dpm.pumping.message

import org.springframework.stereotype.Service

@Service
data class MessageService(
    private val messageRepository: MessageRepository
) {

    fun getMessage(): String {
        val idx = (0..MESSAGE_SIZE).random()
        val message = messageRepository.findByIndex(idx)
            ?: throw IllegalArgumentException("message를 찾을 수 없습니다.index:${idx}")

        return message.content
    }

    companion object{
        private const val MESSAGE_SIZE = 21
    }
}
