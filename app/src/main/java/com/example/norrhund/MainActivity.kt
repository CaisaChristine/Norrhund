package com.example.norrhund

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.norrhund.ui.theme.NorrhundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NorrhundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NorrhundTheme {
        Greeting("Android")
    }
}

data class Dog(
    val id: Int,
    val name: String,
    val age: Int,
    val breed: String,
    val info: String?,
    val checkedIn: Boolean,
)

val startDogs = listOf(
    Dog(1, "Caisa", 7, "Akita", "", true),
    Dog(2, "Oso", 3, "Akita", "Snappy", true),
    Dog(3, "Arya", 1, "Akita", "Food-motivated", true),
    Dog(4, "Ayumi", 1, "Akita", "Energetic", true)
)