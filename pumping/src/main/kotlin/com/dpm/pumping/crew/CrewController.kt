package com.dpm.pumping.crew

import com.dpm.pumping.crew.dto.CreateCrewRequest
import com.dpm.pumping.crew.dto.CrewResponse
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/crew")
@Api(tags = ["크루 관련 API"])
class CrewController(private val crewService: CrewService) {

    // 크루 생성 API
    @PostMapping("/create")
    fun createCrew(@RequestBody request: CreateCrewRequest): ResponseEntity<CrewResponse> {
        val response = crewService.createCrew(request)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    // 크루 조회 API (by crewId)
    @GetMapping("/{crewId}")
    fun getCrew(@PathVariable crewId: String): ResponseEntity<CrewResponse> {
        val response = crewService.getCrew(crewId)
        return ResponseEntity(response, HttpStatus.OK)
    }
}
