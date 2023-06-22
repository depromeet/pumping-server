package com.dpm.pumping.timer

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "timer")
data class Timer(
    @Id
    val timerId: String?,
    val userId: String,
    var time: String?,
    var category: List<String>? = emptyList(), // 카테고리를 List<String> 타입
    var calories: String?,
    var count: String?,
    var heartbeat: String?,
    var kg: String?,
    var sets: String?,
    var machineType: String?
)