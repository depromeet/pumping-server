package com.dpm.pumping.timer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/timer")
class TimerController {

    @Autowired
    lateinit var timerRepository: TimerRepository

    // 특정 유저가 타이머 생성
    @PostMapping("/{userId}")
    fun createTimer(
        @PathVariable userId: String,
        @RequestBody timerBody: Map<String, Any>
    ): ResponseEntity<Timer> {
        // 최대한 명시적으로 toString 처리
        val newTimer = Timer(
            timerId = null,
            userId = userId,
            time = timerBody["time"]?.toString() ?: "",
            category = (timerBody["category"] as? List<*>)?.mapNotNull { it?.toString() },   // 여러 부위 선택이 가능하므로 List<String>으로 받음
            calories = timerBody["calories"]?.toString(),
            count = timerBody["count"]?.toString(),
            heartbeat = timerBody["heartbeat"]?.toString(),
            kg = timerBody["kg"]?.toString(),
            sets = timerBody["sets"]?.toString(),
            machineType = timerBody["machineType"]?.toString()
        )

        val createdTimer = timerRepository.save(newTimer)
        return ResponseEntity(createdTimer, HttpStatus.CREATED)
    }

    // 타이머 ID 를 통한 조회 API
    @GetMapping("/{timerId}")
    fun getTimer(@PathVariable timerId: String): ResponseEntity<Timer> {
        val timer = timerRepository.findById(timerId)
        return timer.map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    // 특정 유저가 생성한 타이머 목록 조회
    @GetMapping("/user/{userId}")
    fun getUserTimers(@PathVariable userId: String): ResponseEntity<List<Timer>> {
        val userTimers = timerRepository.findByUserId(userId)
        return ResponseEntity.ok(userTimers)
    }
}
