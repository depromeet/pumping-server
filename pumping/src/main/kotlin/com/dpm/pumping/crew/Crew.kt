package com.dpm.pumping.crew

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "crew")
data class Crew(
    @Id
    val crewId: String?,
    val crewName: String?,
    val code: String?,
    val createDate: String?,
    val goalCount: Int?,
    val participants: List<String>?
)

