package com.dpm.pumping.crew

import com.dpm.pumping.auth.config.LoginUser
import com.dpm.pumping.user.domain.User
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/crew")
@Api(tags = ["크루 관련 API"])
class CrewController {

    @PostMapping("/create")
    @ApiOperation("크루 생성", notes = "크루를 생성합니다.")
    fun createCrew(@RequestBody crew: Crew, @LoginUser user: User): String {
        return "Crew created successfully"
    }

    @PostMapping("/join/{code}")
    @ApiOperation("크루 참여", notes = "코드를 사용하여 크루에 참여합니다.")
    fun joinCrew(@PathVariable code: String): String {
        return "Joined the crew successfully"
    }

    @PostMapping("/invite")
    @ApiOperation("크루 초대", notes = "사용자를 크루에 초대합니다.")
    fun inviteUser(@RequestParam userId: String, @RequestParam crewId: String): String {
        return "Invitation sent successfully"
    }
}
