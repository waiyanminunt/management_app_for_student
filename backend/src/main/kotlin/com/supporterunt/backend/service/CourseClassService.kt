package com.supporterunt.backend.service

import com.supporterunt.backend.entity.CourseClass
import com.supporterunt.backend.repository.CourseClassRepository
import com.supporterunt.backend.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class CourseClassService(
    private val courseClassRepository: CourseClassRepository,
    private val userRepository: UserRepository
) {

    fun getAllClasses(): List<CourseClass> {
        return courseClassRepository.findAll()
    }

    fun getClassesByTeacher(teacherId: Long): List<CourseClass> {
        return courseClassRepository.findByTeacherId(teacherId)
    }

    fun createClass(title: String, description: String, teacherId: Long, price: Double): CourseClass {
        val teacher = userRepository.findById(teacherId)
            .orElseThrow { IllegalArgumentException("Teacher not found") }

        val newClass = CourseClass(
            title = title,
            description = description,
            teacher = teacher,
            price = price
        )
        return courseClassRepository.save(newClass)
    }
}
