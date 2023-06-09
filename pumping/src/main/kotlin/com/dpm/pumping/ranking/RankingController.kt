package com.dpm.pumping.ranking

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RankingController {
    @GetMapping("/ranking/upper-body")
    fun getUpperBodyRanking(): String {
        return "상체 머신 - 선택했던 부위 중 상체 기준"
    }
    
    @GetMapping("/ranking/total-time")
    fun getTotalTimeRanking(): String {
        return "운동 누적 시간 - 전체 누적시간 기준"
    }
    
    @GetMapping("/ranking/last-place")
    fun getLastPlaceRanking(): String {
        return "꼴찌의 전당 - 전체 누적시간 기준"
    }
    
    @GetMapping("/ranking/lower-body")
    fun getLowerBodyRanking(): String {
        return "하체 머신 - 선택했던 부위 중 하체 기준"
    }
    
    @GetMapping("/ranking/cardio")
    fun getCardioRanking(): String {
        return "유산소킹 - 선택했던 부위 중 유산소 기준"
    }
}