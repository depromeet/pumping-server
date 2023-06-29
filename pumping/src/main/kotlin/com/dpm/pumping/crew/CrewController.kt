package com.dpm.pumping.crew

import com.dpm.pumping.auth.config.LoginUser
import com.dpm.pumping.user.domain.User
import com.dpm.pumping.crew.dto.CreateCrewRequest
import com.dpm.pumping.crew.dto.CrewResponse
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/crews")
@Api(tags = ["크루 관련 API"])
class CrewController(
    private val crewService: CrewService
) {

    @PostMapping
    fun createCrew(@RequestBody request: CreateCrewRequest, @LoginUser user: User): ResponseEntity<CrewResponse> {
        val response = crewService.create(request, user)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    // 크루 참여 API (by code)
    @PostMapping("/join/{code}")
    fun joinCrew(@PathVariable code: String, @LoginUser user: User): ResponseEntity<CrewResponse> {
        val response = crewService.joinCrew(code, user)
        return ResponseEntity(response, HttpStatus.OK)
    }

    // 참여한 크루 조회 (by userId)
    @GetMapping
    fun getCrews(@LoginUser user: User): ResponseEntity<List<Map<String, String?>>> {
        val response = crewService.getCrews(user)
        return ResponseEntity(response, HttpStatus.OK)
    }

}
