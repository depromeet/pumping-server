package com.dpm.pumping.crew

import org.springframework.data.mongodb.repository.MongoRepository

interface CrewRepository : MongoRepository<Crew, String> {
    fun findByCode(code: String): Crew?
    fun findAllCrewsByParticipantsContains(userId: String): List<Crew>
}