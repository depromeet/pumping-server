package com.dpm.pumping.record

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RecordController {
    @GetMapping("/record")
    fun getLogin(): String {
        return "Get Record"
    }
}