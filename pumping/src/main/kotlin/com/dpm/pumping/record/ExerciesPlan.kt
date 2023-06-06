package com.dpm.pumping.record

enum class BodyPart {
    FULL_BODY,
    UPPER_BODY,
    LOWER_BODY
}

data class ExercisePlan(
    val bodyPart: BodyPart
    // Additional properties as per your requirements
)
