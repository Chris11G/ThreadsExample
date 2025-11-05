package edu.farmingdale.threadsexample

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun FibonacciDemoBgThrd() {
    var number by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Fibonacci Calculator (Background Thread)", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = number,
            onValueChange = { number = it },
            label = { Text("Enter number") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            // Launch coroutine to compute in background
            result = "Calculating..."
        }) {
            Text("Compute Fibonacci")
        }

        Text(text = result)
    }

    // Whenever "result" is set to "Calculating...", start background computation
    LaunchedEffect(result) {
        if (result == "Calculating...") {
            val n = number.toIntOrNull()
            if (n != null) {
                val fibValue = withContext(Dispatchers.Default) {
                    fib(n)
                }
                result = "Fibonacci($n) = $fibValue"
            } else {
                result = "Invalid input."
            }
        }
    }
}

// Simple recursive Fibonacci
fun fib(n: Int): Int {
    return if (n <= 1) n else fib(n - 1) + fib(n - 2)
}
