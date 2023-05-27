package com.dpm.pumping.ranking

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RankingController {
    @GetMapping("/ranking")
    fun getLogin(): String {
        return "Get Ranking"
    }
}