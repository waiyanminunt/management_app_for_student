package com.example.supporterunt.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TeacherDashboardScreen(
    onNavigateToMyClasses: () -> Unit,
    onNavigateToMarkAttendance: () -> Unit,
    onNavigateToRoutines: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Teacher Dashboard") },
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
            ElevatedCard(onClick = onNavigateToMyClasses, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text("Manage My Classes", modifier = Modifier.padding(16.dp))
            }
            ElevatedCard(onClick = onNavigateToMarkAttendance, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text("Mark Attendance", modifier = Modifier.padding(16.dp))
            }
            ElevatedCard(onClick = onNavigateToRoutines, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text("Daily Routines", modifier = Modifier.padding(16.dp))
            }
        }
    }
}
