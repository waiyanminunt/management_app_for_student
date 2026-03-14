package com.example.supporterunt.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Placeholder data class until API model is integrated
data class UICourseClass(val id: Long, val title: String, val description: String, val price: Double)

@Composable
fun ClassesScreen(
    onBack: () -> Unit
) {
    val classesList = remember { 
        listOf(
            UICourseClass(1, "Mathematics 101", "Basic Algebra", 15.0),
            UICourseClass(2, "Physics", "Intro to Mechanics", 20.0)
        ) 
    }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Available Classes") },
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
            items(classesList) { course ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(course.title, style = MaterialTheme.typography.titleLarge)
                        Text(course.description)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Price: $${course.price}")
                            Button(onClick = { /* TODO: Call Enroll API */ }) {
                                Text("Enroll")
                            }
                        }
                    }
                }
            }
        }
    }
}
