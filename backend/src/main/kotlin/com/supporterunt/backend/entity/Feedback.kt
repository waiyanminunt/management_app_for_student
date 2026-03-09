package com.supporterunt.backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "feedback")
class Feedback(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    var student: User,

    @Column(name = "season_activity_id", nullable = false)
    var seasonActivityId: Long, // Or create a SeasonActivity entity

    @Column(nullable = false, length = 1000)
    var content: String,

    @Column(nullable = true)
    var vote: Int? = null // For voting positive/negative or 1-5 rating
)
