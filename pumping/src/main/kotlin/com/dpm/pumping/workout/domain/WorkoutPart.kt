package com.dpm.pumping.workout.domain

import com.dpm.pumping.workout.domain.MachineType.*

enum class WorkoutPart(
    private val partName: String,
    private val machines: List<MachineType>
) {

    AEROBIC("유산소", emptyList()),

    SHOULDER("어깨", listOf(SP, SLL, DSP, AHP)),

    CHEST("가슴", listOf(CP, MP, MF, BP, SIB)),

    ARM("팔", listOf(DK, BK, KK, AM, MK)),

    BACK("등", listOf(LD, LOP, SD, BR)),

    HIP("엉덩이", listOf(HA, HS, STD)),

    LEG("다리", listOf(LE, LGP, LK, DL, SQ));

    companion object {
        fun from(name: String): WorkoutPart {
            return values()
                .firstOrNull { value -> value.name == name }
                ?: throw IllegalArgumentException("${name}과 일치하는 부위가 존재하지 않습니다.")
        }

        fun getByMachine(machineName: String): WorkoutPart {
            return values()
                .firstOrNull { it.isIncluded(MachineType.from(machineName)) }
                ?: throw IllegalArgumentException("${machineName}이 속해있는 운동 부위가 존재하지 않습니다.")
        }
    }

    private fun isIncluded(machineType: MachineType): Boolean {
        return this.machines.contains(machineType)
    }
}
