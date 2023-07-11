package com.dpm.pumping.workout.domain.entity

import com.dpm.pumping.workout.domain.MachineType
import com.dpm.pumping.workout.dto.WorkoutCreateDto

class WorkoutSet(
    val machineType: String,
    val kg: String,
    val sets: String
) {
    companion object {
        fun from(request: WorkoutCreateDto.WorkoutSetDto): WorkoutSet {
            return WorkoutSet(
                machineType = MachineType.from(request.machine).name,
                kg = request.kg.toString(),
                sets = request.sets.toString()
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other === this)
            return true

        if (other !is WorkoutSet)
            return false

        return other.machineType == machineType && other.kg == kg && other.sets == sets
    }

    override fun hashCode(): Int {
        var result = machineType.hashCode()
        result = 31 * result + kg.hashCode()
        result = 31 * result + sets.hashCode()
        return result
    }
}
