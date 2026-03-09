package com.supporterunt.backend.controller

import com.supporterunt.backend.entity.DailyRoutine
import com.supporterunt.backend.service.DailyRoutineService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

data class CreateRoutineRequest(val teacherId: Long, val details: String, val date: LocalDate)

@RestController
@RequestMapping("/api/routines")
class DailyRoutineController(
    private val dailyRoutineService: DailyRoutineService
) {

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')") // Only teachers create their own routines
    fun createRoutine(@RequestBody request: CreateRoutineRequest): ResponseEntity<DailyRoutine> {
        val routine = dailyRoutineService.createRoutine(
            request.teacherId,
            request.details,
            request.date
        )
        return ResponseEntity(routine, HttpStatus.CREATED)
    }

    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')") // Both might be able to see Teacher Routines depending on logic
    fun getTeacherRoutines(@PathVariable teacherId: Long): ResponseEntity<List<DailyRoutine>> {
        return ResponseEntity.ok(dailyRoutineService.getRoutinesByTeacher(teacherId))
    }
}
