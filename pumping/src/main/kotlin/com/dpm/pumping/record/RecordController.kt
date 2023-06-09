package com.dpm.pumping.record

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/record")
class RecordController {

    @PostMapping("/plan")
    fun saveExercisePlan(@RequestBody exercisePlan: ExercisePlan): String {
        // Logic to save exercise plan
        return "Exercise plan saved successfully"
    }

    @PostMapping("/result")
    fun saveExerciseResult(@RequestBody exerciseResult: ExerciseResult): String {
        // Logic to save exercise result
        return "Exercise result saved successfully"
    }
}
