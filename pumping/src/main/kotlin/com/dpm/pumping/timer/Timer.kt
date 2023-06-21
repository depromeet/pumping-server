package com.dpm.pumping.timer

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "timer")
data class Timer(
    @Id
    val timerId: String?,
    val userId: String,
    val time: String,
    val category: String?,
    val calories: String?,
    val count: String?,
    val heartbeat: String?,
    val kg: String?,
    val sets: String?,
    val machineType: String?
)