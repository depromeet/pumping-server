package com.dpm.pumping.workout.domain

import com.dpm.pumping.workout.domain.MachineType.*

enum class WorkoutPart(
    private val partName: String,
    private val machines: List<MachineType>
) {

    AEROBIC("유산소", emptyList()),

    SHOULDER("어깨", listOf(SHOULDER_PRESS)),

    CHEST("가슴", listOf(CHEST_PRESS)),

    ARM("팔", listOf(DUMBBELL_KICK)),

    BACK("등", listOf(LATPULL_DOWN)),

    HIP("엉덩이", listOf(HEAP_ADDUCTION)),

    LEG("다리", listOf(LEG_EXTENSION));


    companion object {
        fun from(name: String): WorkoutPart {
            return values()
                .firstOrNull { value -> value.partName == name }
                ?: throw IllegalArgumentException("${name}과 일치하는 부위가 존재하지 않습니다.")
        }

        fun getWorkOutPartByMachine(machineName: String): WorkoutPart {
            return values()
                .firstOrNull { it.isIncluded(MachineType.from(machineName)) }
                ?: throw IllegalArgumentException("${machineName}이 속해있는 운동 부위가 존재하지 않습니다.")
        }
    }

    private fun isIncluded(machineType: MachineType): Boolean {
        return this.machines.contains(machineType)
    }
}