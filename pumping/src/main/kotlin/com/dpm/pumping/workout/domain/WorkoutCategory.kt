package com.dpm.pumping.workout.domain

import com.dpm.pumping.workout.domain.WorkoutPart.*


enum class WorkoutCategory(
    private val workName: String,
    private val workoutParts: List<WorkoutPart>
) {

    WHOLE("전신", listOf(AEROBIC)),

    UP("상체", listOf(SHOULDER, CHEST, ARM, BACK)),

    DOWN("하체", listOf(HIP, LEG));

    companion object {
        fun getWorkOutCategory(name: String): WorkoutCategory {
            return values()
                .firstOrNull { it.isIncluded(WorkoutPart.from(name)) }
                ?: throw IllegalArgumentException("${name}이 속해있는 카테고리가 존재하지 않습니다.")
        }
    }

    private fun isIncluded(workOutPart: WorkoutPart): Boolean {
        return this.workoutParts.contains(workOutPart)
    }
}