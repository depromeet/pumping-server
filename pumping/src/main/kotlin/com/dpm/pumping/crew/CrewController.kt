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
@RequestMapping("/crew")
@Api(tags = ["크루 관련 API"])
class CrewController(private val crewService: CrewService) {

    // 크루 생성 API
    @PostMapping("/create")
    fun createCrew(@RequestBody request: CreateCrewRequest, @LoginUser user: User): ResponseEntity<CrewResponse> {
        val response = crewService.createCrew(request, user)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    // 크루 조회 API (by crewId)
    @GetMapping("/{crewId}")
    fun getCrew(@PathVariable crewId: String, @LoginUser user: User): ResponseEntity<CrewResponse> {
        val response = crewService.getCrew(crewId, user)
        return ResponseEntity(response, HttpStatus.OK)
    }

    // 크루 조회 API (by code)
    @GetMapping("/code/{code}")
    fun getCrewByCode(@PathVariable code: String, @LoginUser user: User): ResponseEntity<CrewResponse> {
        val response = crewService.getCrewByCode(code, user)
        return ResponseEntity(response, HttpStatus.OK)
    }

    // 크루 참여 API (by code)
    @PostMapping("/join/{code}")
    fun joinCrew(@PathVariable code: String, @LoginUser user: User): ResponseEntity<CrewResponse> {
        val response = crewService.joinCrew(code, user)
        return ResponseEntity(response, HttpStatus.OK)
    }

}
