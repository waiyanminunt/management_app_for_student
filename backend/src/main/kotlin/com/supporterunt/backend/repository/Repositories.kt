package com.supporterunt.backend.repository

import com.supporterunt.backend.entity.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
}

@Repository
interface CourseClassRepository : JpaRepository<CourseClass, Long> {
    fun findByTeacherId(teacherId: Long): List<CourseClass>
}

@Repository
interface RegistrationRepository : JpaRepository<Registration, Long> {
    fun findByStudentId(studentId: Long): List<Registration>
    fun findByCourseClassId(courseClassId: Long): List<Registration>
}

@Repository
interface AttendanceRepository : JpaRepository<Attendance, Long> {
    fun findByCourseClassId(courseClassId: Long): List<Attendance>
    fun findByStudentId(studentId: Long): List<Attendance>
}

@Repository
interface DailyRoutineRepository : JpaRepository<DailyRoutine, Long> {
    fun findByTeacherId(teacherId: Long): List<DailyRoutine>
}

@Repository
interface FeedbackRepository : JpaRepository<Feedback, Long> {
    fun findByStudentId(studentId: Long): List<Feedback>
}
