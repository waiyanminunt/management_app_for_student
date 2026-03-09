package com.supporterunt.backend.service

import com.supporterunt.backend.entity.Feedback
import com.supporterunt.backend.repository.FeedbackRepository
import com.supporterunt.backend.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class FeedbackService(
    private val feedbackRepository: FeedbackRepository,
    private val userRepository: UserRepository
) {

    fun createFeedback(studentId: Long, seasonActivityId: Long, content: String, vote: Int? = null): Feedback {
        val student = userRepository.findById(studentId)
            .orElseThrow { IllegalArgumentException("Student not found") }

        val feedback = Feedback(
            student = student,
            seasonActivityId = seasonActivityId,
            content = content,
            vote = vote
        )
        return feedbackRepository.save(feedback)
    }

    fun getAllFeedback(): List<Feedback> {
        return feedbackRepository.findAll()
    }
    
    fun getFeedbackByStudent(studentId: Long): List<Feedback> {
        return feedbackRepository.findByStudentId(studentId)
    }
}
