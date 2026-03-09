package com.supporterunt.backend.service

import com.supporterunt.backend.entity.Registration
import com.supporterunt.backend.repository.CourseClassRepository
import com.supporterunt.backend.repository.RegistrationRepository
import com.supporterunt.backend.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class RegistrationService(
    private val registrationRepository: RegistrationRepository,
    private val userRepository: UserRepository,
    private val courseClassRepository: CourseClassRepository
) {

    fun enrollStudent(studentId: Long, courseClassId: Long): Registration {
        val student = userRepository.findById(studentId)
            .orElseThrow { IllegalArgumentException("Student not found") }
        val courseClass = courseClassRepository.findById(courseClassId)
            .orElseThrow { IllegalArgumentException("Course Class not found") }

        // Check if already registered
        val existingRegistration = registrationRepository.findByStudentId(studentId)
            .find { it.courseClass.id == courseClassId }

        if (existingRegistration != null) {
            throw IllegalStateException("Student is already enrolled in this class")
        }

        val registration = Registration(
            student = student,
            courseClass = courseClass,
            paymentStatus = "PENDING"
        )

        return registrationRepository.save(registration)
    }

    fun getStudentRegistrations(studentId: Long): List<Registration> {
        return registrationRepository.findByStudentId(studentId)
    }

    fun updatePaymentStatus(registrationId: Long, status: String): Registration {
        val registration = registrationRepository.findById(registrationId)
            .orElseThrow { IllegalArgumentException("Registration not found") }
        registration.paymentStatus = status
        return registrationRepository.save(registration)
    }
}
