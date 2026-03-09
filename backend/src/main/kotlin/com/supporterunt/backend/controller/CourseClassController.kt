package com.supporterunt.backend.controller

import com.supporterunt.backend.entity.CourseClass
import com.supporterunt.backend.service.CourseClassService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

data class CreateClassDto(val title: String, val description: String, val teacherId: Long, val price: Double)

@RestController
@RequestMapping("/api/classes")
class CourseClassController(
    private val courseClassService: CourseClassService
) {

    @GetMapping
    fun getAllClasses(): ResponseEntity<List<CourseClass>> {
        return ResponseEntity.ok(courseClassService.getAllClasses())
    }

    @GetMapping("/teacher/{teacherId}")
    fun getClassesByTeacher(@PathVariable teacherId: Long): ResponseEntity<List<CourseClass>> {
        return ResponseEntity.ok(courseClassService.getClassesByTeacher(teacherId))
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')") // Only teachers can create classes
    fun createClass(@RequestBody request: CreateClassDto): ResponseEntity<CourseClass> {
        val newClass = courseClassService.createClass(
            title = request.title,
            description = request.description,
            teacherId = request.teacherId,
            price = request.price
        )
        return ResponseEntity(newClass, HttpStatus.CREATED)
    }
}
