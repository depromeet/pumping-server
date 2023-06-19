package com.dpm.pumping.user

import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {
    fun saveUser(user: User): User {
        return userRepository.save(user)
    }

    fun getUserByUid(uid: String): User? {
        return userRepository.findByUid(uid)
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun updateUser(user: User): User {
        return userRepository.save(user)
    }

    fun deleteUser(uid: String) {
        userRepository.deleteByUid(uid)
    }
}
