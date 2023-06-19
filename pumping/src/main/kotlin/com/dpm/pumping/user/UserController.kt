package com.dpm.pumping.user

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody user: User): User {
        return userService.saveUser(user)
    }

    @GetMapping("/{uid}")
    fun getUserByUid(@PathVariable uid: String): User? {
        return userService.getUserByUid(uid)
    }

    @GetMapping
    fun getAllUsers(): List<User> {
        return userService.getAllUsers()
    }

    @PutMapping("/{uid}")
    fun updateUser(@PathVariable uid: String, @RequestBody user: User): User {
        val updatedUser = user.copy(uid = uid)
        return userService.updateUser(updatedUser)
    }

    @DeleteMapping("/{uid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable uid: String) {
        userService.deleteUser(uid)
    }
}
