package com.dpm.pumping.user.domain

import com.dpm.pumping.auth.domain.LoginPlatform
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface UserRepository : MongoRepository<User, String> {
    fun findByPlatform(platform: LoginPlatform): User?
    @Query("{'_id': ?0}")
    fun update(id: String, updatedFields: Map<String, Any>): User
}
