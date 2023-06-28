package com.dpm.pumping.crew.dto

data class CreateCrewRequest(
    val crewName: String,
    val goalCount: Int
)

data class CrewResponse(
    val crewId: String?,
    val crewName: String?,
    val goalCount: Int?,
    val code: String?,
    val participants: List<String?>
)