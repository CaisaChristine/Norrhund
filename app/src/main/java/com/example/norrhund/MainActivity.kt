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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment

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
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = dog.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "${dog.age} år")
                Text(text = "Ras: ${dog.breed}")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (dog.checkedIn) "Närvarande" else "Frånvarande",
                    style = MaterialTheme.typography.bodyMedium
                )
                Checkbox(
                    checked = dog.checkedIn,
                    onCheckedChange = onCheckedChange
                )
            }
        }
    }
}

@Composable
fun AddDog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, age: Int, breed: String, info: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var info by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Lägg till hund") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
               TextField(
                   value = name,
                   onValueChange = { name = it },
                   label = { Text("Namn") },
                   singleLine = true,
                   modifier = Modifier.fillMaxWidth()
               )
               TextField(
                   value = age,
                   onValueChange = { age = it },
                   label = { Text("Ålder") },
                   singleLine = true,
                   modifier = Modifier.fillMaxWidth()
               )
               TextField(
                   value = breed,
                   onValueChange = { breed = it },
                   label = { Text("Ras/Raser") },
                   singleLine = true,
                   modifier = Modifier.fillMaxWidth()
               )
               TextField(
                   value = info,
                   onValueChange = { info = it },
                   label = { Text("Bra att veta om hunden") },
                   modifier = Modifier.fillMaxWidth()
               )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val ageInt = age.toIntOrNull() ?: 0
                    if(name.isNotBlank()) {
                        onConfirm(name, ageInt, breed, info)
                    }
                }
            )
            { Text("Spara") }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            )
            { Text("Avbryt") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogViewScreen(viewModel: DogListViewModel = viewModel()) {
    val state = viewModel.uiState
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar =  {
            TopAppBar(
                title = { Text("Välkommen till Norrhund pensionat och dagis") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },

        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Skapa ny hund")
            }
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
                DogCard(
                    dog = dog,
                    onCheckedChange = { isChecked ->
                        viewModel.toggleCheckedIn(dog.id, isChecked)
                    })
            }
        }

        if(showAddDialog) {
            AddDog(
                onDismiss = { showAddDialog = false },
                onConfirm = { name, age, breed, info ->
                    viewModel.addDog(name, age, breed, info)
                    showAddDialog = false
                }
            )
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