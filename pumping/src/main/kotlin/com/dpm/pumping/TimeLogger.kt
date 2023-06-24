package com.dpm.pumping

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/logging")
class TimeLogger(private val mongoTemplate: MongoTemplate) {
    @GetMapping
    fun sayHello(): ResponseEntity<Response> {
        val currentTime = LocalDateTime.now()
        val logEntry = LogEntry(time = currentTime.toString())
        mongoTemplate.save(logEntry, "log")
        return ResponseEntity.ok(Response("hello world"))
    }

    data class Response(
        val result: String
    )

    @Document(collection = "log")
    data class LogEntry(
        @Id
        val id: String? = null,
        val time: String
    )
}