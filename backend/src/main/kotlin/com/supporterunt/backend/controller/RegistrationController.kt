package com.supporterunt.backend.controller

import com.supporterunt.backend.entity.Registration
import com.supporterunt.backend.service.RegistrationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

data class EnrollRequest(val studentId: Long, val courseClassId: Long)
data class UpdatePaymentRequest(val status: String)

@RestController
@RequestMapping("/api/registrations")
class RegistrationController(
    private val registrationService: RegistrationService
) {

    @PostMapping("/enroll")
    @PreAuthorize("hasRole('STUDENT')") // Only students can self-enroll
    fun enroll(@RequestBody request: EnrollRequest): ResponseEntity<Registration> {
        return try {
            val registration = registrationService.enrollStudent(request.studentId, request.courseClassId)
            ResponseEntity(registration, HttpStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping("/student/{studentId}")
    fun getStudentRegistrations(@PathVariable studentId: Long): ResponseEntity<List<Registration>> {
        return ResponseEntity.ok(registrationService.getStudentRegistrations(studentId))
    }

    @PatchMapping("/{id}/payment")
    @PreAuthorize("hasRole('TEACHER')") // Teachers/Admins update payment status
    fun updatePaymentStatus(@PathVariable id: Long, @RequestBody request: UpdatePaymentRequest): ResponseEntity<Registration> {
        return ResponseEntity.ok(registrationService.updatePaymentStatus(id, request.status))
    }
}
