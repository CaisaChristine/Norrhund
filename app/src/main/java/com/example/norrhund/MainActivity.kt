package com.example.norrhund

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.norrhund.ui.theme.NorrhundTheme
import androidx.compose.foundation.lazy.items

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NorrhundTheme {
                    DogViewScreen()
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

@Composable
fun DogCard(
    dog: Dog,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
        ) {
            Text(text = dog.name, style = MaterialTheme.typography.titleMedium)
            Text(text ="${dog.age} år")
            Text(text = "Ras ${dog.breed}")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogViewScreen(viewModel: DogListViewModel = viewModel()) {
    val state = viewModel.uiState
    Scaffold(
        topBar =  {
            TopAppBar(
                title = { Text("Välkommen till Norrhund pensionat och dagis") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(state.dogs, key = { it.id }) { dog ->
                DogCard(dog = dog)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DogListPreview() {
    NorrhundTheme {
        DogViewScreen()
    }
}