package com.supporterunt.backend.service

import com.supporterunt.backend.entity.DailyRoutine
import com.supporterunt.backend.repository.DailyRoutineRepository
import com.supporterunt.backend.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DailyRoutineService(
    private val dailyRoutineRepository: DailyRoutineRepository,
    private val userRepository: UserRepository
) {

    fun createRoutine(teacherId: Long, details: String, date: LocalDate): DailyRoutine {
        val teacher = userRepository.findById(teacherId)
            .orElseThrow { IllegalArgumentException("Teacher not found") }

        val routine = DailyRoutine(
            teacher = teacher,
            details = details,
            date = date
        )
        return dailyRoutineRepository.save(routine)
    }

    fun getRoutinesByTeacher(teacherId: Long): List<DailyRoutine> {
        return dailyRoutineRepository.findByTeacherId(teacherId)
    }
}
