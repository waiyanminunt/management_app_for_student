package com.example.supporterunt.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.example.supporterunt.app.ui.components.QRCodeImage
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
            
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "My Digital Attendance QR",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Hardcoded student ID/Data for demonstration. 
                // In production, this would come from the ViewModel.
                QRCodeImage(
                    content = "STUDENT-ID-12345:ATTENDANCE",
                    modifier = Modifier.size(200.dp)
                )
            }
        }
    }
}
