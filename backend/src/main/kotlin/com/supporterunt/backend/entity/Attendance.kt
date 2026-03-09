package com.supporterunt.backend.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "attendance")
class Attendance(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_class_id", nullable = false)
    var courseClass: CourseClass,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    var student: User,

    @Column(nullable = false)
    var date: LocalDate,

    @Column(nullable = false)
    var status: String = "PRESENT" // e.g. PRESENT, ABSENT, LATE
)
