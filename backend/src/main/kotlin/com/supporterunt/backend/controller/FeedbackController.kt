package com.supporterunt.backend.controller

import com.supporterunt.backend.entity.Feedback
import com.supporterunt.backend.service.FeedbackService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

data class CreateFeedbackRequest(val studentId: Long, val seasonActivityId: Long, val content: String, val vote: Int?)

@RestController
@RequestMapping("/api/feedback")
class FeedbackController(
    private val feedbackService: FeedbackService
) {

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')") // Only students give feedback
    fun submitFeedback(@RequestBody request: CreateFeedbackRequest): ResponseEntity<Feedback> {
        val feedback = feedbackService.createFeedback(
            request.studentId,
            request.seasonActivityId,
            request.content,
            request.vote
        )
        return ResponseEntity(feedback, HttpStatus.CREATED)
    }

    @GetMapping
    fun getAllFeedback(): ResponseEntity<List<Feedback>> {
        return ResponseEntity.ok(feedbackService.getAllFeedback())
    }

    @GetMapping("/student/{studentId}")
    fun getFeedbackByStudent(@PathVariable studentId: Long): ResponseEntity<List<Feedback>> {
        return ResponseEntity.ok(feedbackService.getFeedbackByStudent(studentId))
    }
}
