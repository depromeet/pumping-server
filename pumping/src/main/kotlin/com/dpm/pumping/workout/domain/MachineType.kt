package com.dpm.pumping.workout.domain

enum class MachineType(
    private val machineName: String
) {
    CHEST_PRESS("체스트 프레스"),

    SHOULDER_PRESS("숄더 프레스"),

    DUMBBELL_KICK("덤벨 킥"),

    LATPULL_DOWN("렛풀 다운"),

    HEAP_ADDUCTION("힙 어드덕션 "),

    LEG_EXTENSION("레그 익스텐션");

    companion object {
        fun of(name: String): MachineType {
            return values()
                .firstOrNull { value -> value.machineName == name }
                ?: throw IllegalArgumentException("${name}과 일치하는 운동 기구가 존재하지 않습니다.")
        }
    }
}