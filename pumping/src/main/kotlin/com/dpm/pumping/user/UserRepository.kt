package com.dpm.pumping.user

import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
    fun findByUid(uid: String): User?

    fun deleteByUid(uid: String)
}
