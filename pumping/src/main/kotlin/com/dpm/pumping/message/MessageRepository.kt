package com.dpm.pumping.message

import org.springframework.data.mongodb.repository.MongoRepository

interface MessageRepository : MongoRepository<Message, String>{
    fun findByIndex(idx: Int): Message?

    fun existsByIndex(idx: Int): Boolean
}
