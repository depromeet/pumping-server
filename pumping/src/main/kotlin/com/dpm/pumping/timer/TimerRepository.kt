package com.dpm.pumping.timer

import org.springframework.data.mongodb.repository.MongoRepository

interface TimerRepository : MongoRepository<Timer, String> {
    fun findByUserId(userId: String): List<Timer>
}
