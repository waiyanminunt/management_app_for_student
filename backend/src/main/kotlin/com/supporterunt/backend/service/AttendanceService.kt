package com.supporterunt.backend.service

import com.supporterunt.backend.entity.Attendance
import com.supporterunt.backend.repository.AttendanceRepository
import com.supporterunt.backend.repository.CourseClassRepository
import com.supporterunt.backend.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AttendanceService(
    private val attendanceRepository: AttendanceRepository,
    private val courseClassRepository: CourseClassRepository,
    private val userRepository: UserRepository
) {

    fun markAttendance(courseClassId: Long, studentId: Long, date: LocalDate, status: String): Attendance {
        val courseClass = courseClassRepository.findById(courseClassId)
            .orElseThrow { IllegalArgumentException("Class not found") }
        val student = userRepository.findById(studentId)
            .orElseThrow { IllegalArgumentException("Student not found") }

        val attendance = Attendance(
            courseClass = courseClass,
            student = student,
            date = date,
            status = status
        )
        return attendanceRepository.save(attendance)
    }

    fun getAttendanceByClass(courseClassId: Long): List<Attendance> {
        return attendanceRepository.findByCourseClassId(courseClassId)
    }

    fun getAttendanceByStudent(studentId: Long): List<Attendance> {
        return attendanceRepository.findByStudentId(studentId)
    }
}
