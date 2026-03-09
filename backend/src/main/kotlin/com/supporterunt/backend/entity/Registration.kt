package com.supporterunt.backend.entity

import jakarta.persistence.*

@Entity
@Table(name = "registrations")
class Registration(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    var student: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_class_id", nullable = false)
    var courseClass: CourseClass,

    @Column(nullable = false)
    var paymentStatus: String = "PENDING" // e.g. PENDING, PAID
)
