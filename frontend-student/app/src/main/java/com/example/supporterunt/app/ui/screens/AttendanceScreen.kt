package com.example.supporterunt.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun AttendanceScreen(
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Attendance") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text("Here is where the attendance list goes.", modifier = Modifier.padding(16.dp))
            // Example of a small form logic
            Button(onClick = { /* Call mark attendance API */ }, modifier = Modifier.padding(16.dp)) {
                Text("Mark Present Today")
            }
        }
    }
}
