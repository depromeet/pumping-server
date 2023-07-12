package com.dpm.pumping.workout.util

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime


object CalenderUtils {

    private const val TOTAL_DAYS_OF_WEEK = 7

    fun getDayOfWeek(date: LocalDate): Int {
        val dayOfWeek = date.dayOfWeek
        return dayOfWeek.value
    }

    fun getWeek(startDate: LocalDateTime, curDate: LocalDateTime): Int {
        val duration = Duration.between(startDate, curDate).toDays()
        val week = (duration / TOTAL_DAYS_OF_WEEK) + 1
        return week.toInt()
    }
}
