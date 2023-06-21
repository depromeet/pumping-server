package com.dpm.pumping.timer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest

@DataMongoTest
class TimerRepositoryTest(
    @Autowired val timerRepository: TimerRepository,
) {

    @BeforeEach
    fun setUp() {
        timerRepository.deleteAll()
    }

    @Test
    fun testCreateRecord() {
        val timer = Timer(
            timerId = "timer1",
            userId = "user1",
            time = "2023-06-22T10:00:00+09:00",
            category = "whole",
            calories = "100",
            count = "10",
            heartbeat = "80",
            kg = "50",
            sets = "3",
            machineType = "treadmill"
        )
        val savedRecord = timerRepository.save(timer)
        val foundRecord = timerRepository.findById(savedRecord.timerId.orEmpty())

        assertThat(foundRecord.isPresent).isTrue()
        assertThat(foundRecord.get()).isEqualTo(savedRecord)
    }
}
