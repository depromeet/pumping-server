package com.dpm.pumping.user.domain

import com.dpm.pumping.auth.domain.LoginPlatform
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
    fun findByPlatform(platform: LoginPlatform): User?
}