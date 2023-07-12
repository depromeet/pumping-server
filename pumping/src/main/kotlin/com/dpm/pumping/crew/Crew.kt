package com.dpm.pumping.crew

import com.dpm.pumping.workout.application.WorkoutStorage
import com.dpm.pumping.workout.util.CalenderUtils
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Document(collection = "crew")
data class Crew(
    @Id
    val crewId: String?,
    val crewName: String?,
    val code: String?,
    val createDate: String?,
    val goalCount: Int?,
    val participants: List<String?>
){

    companion object {
        fun create(name: String, goalCount: Int, userId: String): Crew {
            return Crew(
                crewId = UUID.randomUUID().toString(),
                crewName = name,
                code = generateCrewCode(), // 크루 코드 생성
                createDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                goalCount = goalCount,
                participants = listOf(userId) // 크루 생성자는 참여자로 자동 등록
            )
        }

        private fun generateCrewCode(): String {
            return (1..999999).random().toString().padStart(6, '0')
        }
    }

    fun getStartDate(): LocalDateTime {
        val startDate = LocalDateTime.parse(createDate!!)
        val week = CalenderUtils.getWeek(startDate, LocalDateTime.now())
        return startDate.plusDays((week - 1) * WorkoutStorage.DEFAULT_SIZE)
    }
}



