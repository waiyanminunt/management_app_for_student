package com.supporterunt.backend.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "daily_routines")
class DailyRoutine(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    var teacher: User,

    @Column(nullable = false, length = 1000)
    var details: String,

    @Column(nullable = false)
    var date: LocalDate
)
