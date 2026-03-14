package com.example.supporterunt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.supporterunt.app.ui.screens.*
import com.example.supporterunt.ui.theme.SupporterUntTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SupporterUntTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // A simple router for the application
                    var currentScreen by remember { mutableStateOf("LOGIN") }

                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            "LOGIN" -> LoginScreen(
                                onLoginSuccess = { role -> 
                                    currentScreen = if (role == "TEACHER") "TEACHER_DASHBOARD" else "STUDENT_DASHBOARD" 
                                },
                                onNavigateToRegister = { currentScreen = "REGISTER" }
                            )
                            "REGISTER" -> RegistrationScreen(
                                onRegisterSuccess = { currentScreen = "LOGIN" },
                                onNavigateToLogin = { currentScreen = "LOGIN" }
                            )
                            "STUDENT_DASHBOARD" -> StudentDashboardScreen(
                                onNavigateToClasses = { currentScreen = "CLASSES" },
                                onNavigateToAttendance = { currentScreen = "ATTENDANCE" },
                                onNavigateToFeedback = { currentScreen = "FEEDBACK" },
                                onLogout = { currentScreen = "LOGIN" }
                            )
                            "TEACHER_DASHBOARD" -> TeacherDashboardScreen(
                                onNavigateToMyClasses = { currentScreen = "CLASSES" },
                                onNavigateToMarkAttendance = { currentScreen = "ATTENDANCE" },
                                onNavigateToRoutines = { currentScreen = "ROUTINES" },
                                onLogout = { currentScreen = "LOGIN" }
                            )
                            "CLASSES" -> ClassesScreen(onBack = { currentScreen = "STUDENT_DASHBOARD" })
                            "ATTENDANCE" -> AttendanceScreen(onBack = { currentScreen = "STUDENT_DASHBOARD" })
                            "ROUTINES" -> DailyRoutineScreen(onBack = { currentScreen = "TEACHER_DASHBOARD" })
                            "FEEDBACK" -> FeedbackScreen(onBack = { currentScreen = "STUDENT_DASHBOARD" })
                        }
                    }
                }
            }
        }
    }
}
