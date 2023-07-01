package com.dpm.pumping.workout.domain

enum class MachineType(
    private val machineName: String,
) {
    CP("체스트 프레스"),

    MP("멀티 프레스"),

    MF("머신 플라이"),

    BP("벤치 프레스"),

    SIB("스미스 인크라인 벤치프레스"),

    SP("숄더 프레스"),

    SLL("사이드 레터럴 레이즈"),

    DSP("덤벨 숄더 프레스"),

    AHP("오버 헤드 프레스"),

    DK("덤벨 킥"),

    BK("바벨 컬"),

    KK("케이블 컬"),

    AM("암컬 머신"),

    MK("머신 컬"),

    LD("렛풀 다운"),

    LOP("롱풀"),

    SD("시티 드로우"),

    BR("바벨 로우"),

    HA("힙 어드덕션"),

    HS("힙 쓰러스트"),

    STD("스티프 데드리프트"),

    LE("레그 익스텐션"),

    LGP("레그 프레스"),

    LK("레그 컬"),

    DL("덤벨 런지"),

    SQ("스쿼트");

    companion object {
        fun from(name: String): MachineType {
            return values()
                .firstOrNull { value -> value.name == name }
                ?: throw IllegalArgumentException("${name}과 일치하는 운동 기구가 존재하지 않습니다.")
        }
    }
}
