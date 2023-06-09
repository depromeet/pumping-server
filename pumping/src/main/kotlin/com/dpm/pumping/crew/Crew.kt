package com.dpm.pumping.crew

data class Crew(
    val name: String,
    val count: Int,
    val duration: String,
    val members: List<CrewMember>
)
