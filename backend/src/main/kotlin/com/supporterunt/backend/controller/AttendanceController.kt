package com.supporterunt.backend.controller

import com.supporterunt.backend.entity.Attendance
import com.supporterunt.backend.service.AttendanceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

data class MarkAttendanceRequest(val courseClassId: Long, val studentId: Long, val date: LocalDate, val status: String)

@RestController
@RequestMapping("/api/attendance")
class AttendanceController(
    private val attendanceService: AttendanceService
) {

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')") // Only teachers mark attendance
    fun markAttendance(@RequestBody request: MarkAttendanceRequest): ResponseEntity<Attendance> {
        val attendance = attendanceService.markAttendance(
            request.courseClassId,
            request.studentId,
            request.date,
            request.status
        )
        return ResponseEntity(attendance, HttpStatus.CREATED)
    }

    @GetMapping("/class/{classId}")
    @PreAuthorize("hasRole('TEACHER')")
    fun getAttendanceByClass(@PathVariable classId: Long): ResponseEntity<List<Attendance>> {
        return ResponseEntity.ok(attendanceService.getAttendanceByClass(classId))
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    fun getAttendanceByStudent(@PathVariable studentId: Long): ResponseEntity<List<Attendance>> {
        return ResponseEntity.ok(attendanceService.getAttendanceByStudent(studentId))
    }
}
