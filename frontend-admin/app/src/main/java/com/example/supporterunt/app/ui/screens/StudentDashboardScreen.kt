package com.example.supporterunt.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StudentDashboardScreen(
    onNavigateToClasses: () -> Unit,
    onNavigateToAttendance: () -> Unit,
    onNavigateToFeedback: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Student Dashboard") },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ElevatedCard(onClick = onNavigateToClasses, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text("Browse & Enrol Classes", modifier = Modifier.padding(16.dp))
            }
            ElevatedCard(onClick = onNavigateToAttendance, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text("View My Attendance", modifier = Modifier.padding(16.dp))
            }
            ElevatedCard(onClick = onNavigateToFeedback, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text("Give Feedback / Voting", modifier = Modifier.padding(16.dp))
            }
        }
    }
}
