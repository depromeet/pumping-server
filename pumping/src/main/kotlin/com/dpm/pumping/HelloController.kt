package com.dpm.pumping

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/hello")
class HelloController {

    @GetMapping
    fun sayHello(): Response {
        return Response("hello world")
    }

    data class Response (
        val result: String
    )
}