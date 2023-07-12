package com.dpm.pumping.message

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "message")
data class Message(
    @Id
    val id: String? = null,

    val index: Int,

    val content: String
)
