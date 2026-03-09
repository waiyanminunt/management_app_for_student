package com.supporterunt.backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "course_classes")
class CourseClass(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false)
    var description: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    var teacher: User,

    @Column(nullable = false)
    var price: Double = 0.0
)
