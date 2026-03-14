package com.example.supporterunt.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class UIRoutine(val id: Long, val details: String, val date: String)

@Composable
fun DailyRoutineScreen(
    onBack: () -> Unit
) {
    val routines = remember {
        listOf(
            UIRoutine(1, "Prepare Physics slides", "2026-03-06"),
            UIRoutine(2, "Grade Math homeworks", "2026-03-06")
        )
    }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Daily Routines") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                Text("Your Scheduled Routines", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
            }
            items(routines) { routine ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(routine.date, style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(routine.details, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
            item {
                Button(onClick = { /* TODO: Create Routine via API */ }, modifier = Modifier.padding(16.dp)) {
                    Text("Add New Routine")
                }
            }
        }
    }
}
