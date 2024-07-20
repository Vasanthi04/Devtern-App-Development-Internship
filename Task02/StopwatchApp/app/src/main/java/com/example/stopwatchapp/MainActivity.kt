package com.example.stopwatchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stopwatchapp.ui.theme.StopwatchAppTheme
import kotlinx.coroutines.delay
import kotlin.math.floor
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StopwatchAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StopwatchScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun StopwatchScreen(modifier: Modifier = Modifier) {
    var isRunning by remember { mutableStateOf(false) }
    var seconds by remember { mutableIntStateOf(0) }
    var lapTimes by remember { mutableStateOf(listOf<String>()) }

    // Timer logic
    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000L) // 1 second delay
            seconds++
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title Text
        Text(
            text = "STOP WATCH",
            fontSize = 32.sp,
            color = Color.Blue, // You can change the color to whatever you like
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Time Display
        Text(
            text = formatTime(seconds),
            fontSize = 48.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { isRunning = true }) {
                Text("Start")
            }
            Button(onClick = { isRunning = false }) {
                Text("Stop")
            }
            Button(onClick = {
                isRunning = false
                seconds = 0
            }) {
                Text("Reset")
            }
            Button(onClick = {
                lapTimes = lapTimes + formatTime(seconds)
            }) {
                Text("Lap")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lap Times Display
        lapTimes.forEach {
            Text(text = it)
        }
    }
}

fun formatTime(seconds: Int): String {
    val minutes = floor(seconds / 60.0).toInt()
    val hours = floor(minutes / 60.0).toInt()
    val remainingSeconds = seconds % 60
    val remainingMinutes = minutes % 60
    return String.format(Locale.US, "%02d:%02d:%02d", hours, remainingMinutes, remainingSeconds)
}

@Preview(showBackground = true)
@Composable
fun StopwatchScreenPreview() {
    StopwatchAppTheme {
        StopwatchScreen()
    }
}
