package com.example.norrhund

import androidx.compose.ui.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pets
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType

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
    Dog(1, "Caisa", 7, "Akita", null, false),
    Dog(2, "Oso", 3, "Akita", "Snappy", false),
    Dog(3, "Arya", 1, "Akita", "Mat-motiverad", false),
    Dog(4, "Ayumi", 1, "Akita", "Energisk", false)
)
@Composable
fun DogCard(
    dog: Dog,
    onCardClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onCardClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Pets,
                        contentDescription = null,
                        tint = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = dog.name,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            Text(
                text = "${dog.age} år",
                style = MaterialTheme.typography.bodyMedium
                )
            Text(
                text = "Ras: ${dog.breed}",
                style = MaterialTheme.typography.bodyMedium
            )
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
                IconButton(onClick = onUpdateClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Ändra ${dog.name}"
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Ta bort ${dog.name}",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun ViewDogInfo(
    dog: Dog,
    onDismiss: () -> Unit
) {
    AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(dog.name) },
    text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Ålder: ${dog.age} år", style = MaterialTheme.typography.bodyLarge)
            Text("Ras: ${dog.breed}", style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "Status: ${if (dog.checkedIn) "Närvarande" else "Frånvarande"}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Bra att veta om hunden ->",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = dog.info ?: "Ingen extra information angiven.",
                style = MaterialTheme.typography.bodyMedium,
                color = if(dog.info == null) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Stäng")
            }
        }
    )
}

@Composable
fun AddDog(
    dogToUpdate: Dog? = null,
    onDismiss: () -> Unit,
    onConfirm: (name: String, age: Int, breed: String, info: String?) -> Unit
) {
    var name by remember { mutableStateOf(dogToUpdate?.name ?: "") }
    var age by remember { mutableStateOf(dogToUpdate?.age?.toString() ?: "") }
    var breed by remember { mutableStateOf(dogToUpdate?.breed ?: "") }
    var info by remember { mutableStateOf(dogToUpdate?.info ?: "") }

    val isFormValid = name.isNotBlank() && age.isNotBlank() && breed.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if(dogToUpdate == null) "Skapa en ny hund" else "Ändra ${dogToUpdate.name}")},
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
                   onValueChange = { newValue ->
                       if(newValue.all { it.isDigit() } && newValue.length <= 2) {
                           age = newValue
                       }
                   },
                   label = { Text("Antal år") },
                   singleLine = true,
                   keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                    if(isFormValid && ageInt != null) {
                        val infoOrNull = info.trim().ifEmpty { null }
                        onConfirm(name.trim(), ageInt, breed.trim(), infoOrNull)
                    }
                },
                enabled = isFormValid
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

@Composable
fun DeleteDog(
    dogName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Radera hund") },
        text = { Text("Är du säker på att du vill radera $dogName?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Radera")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Avbryt")
            }
        }
    )
}

@Composable
fun ListCheckedInDogs(
    dogs: List<Dog>,
    onDismiss: () -> Unit
) {
    val checkedInDogs = dogs.filter { it.checkedIn }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Incheckade hundar just nu (${checkedInDogs.size})") },
        text = {
            if(checkedInDogs.isEmpty()) {
                Text("Inga hundar incheckade för tillfället.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(checkedInDogs, key = { it.id }) { dog ->
                        Column {
                            Text(
                                text = "• ${dog.name} (Ras: ${dog.breed}, ${dog.age} år)",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            if(!dog.info.isNullOrBlank()) {
                                Text(
                                    text = "Bra att veta: ${dog.info}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Stäng")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DogViewScreen(viewModel: DogListViewModel = viewModel()) {
    val state = viewModel.uiState
    val checkedInCount = state.dogs.count { it.checkedIn }

    var showAddDialog by remember { mutableStateOf(false) }
    var showCheckedInDialog by remember { mutableStateOf(false) }
    var dogToUpdate by remember { mutableStateOf<Dog?>(null) }
    var dogToDelete by remember { mutableStateOf<Dog?>(null) }
    var selectedDog by remember { mutableStateOf<Dog?>(null) }

    Scaffold(
        topBar =  {
            TopAppBar(
                title = { Text("Välkommen till Norrhund pensionat och dagis") },
                actions = {
                    TextButton(
                        onClick = { showCheckedInDialog = true },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        ) {
                        Text("Närvarande ($checkedInCount)")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
                    onCardClick = { selectedDog = dog },
                    onCheckedChange = { isChecked ->
                        viewModel.toggleCheckedIn(dog.id, isChecked)
                    },
                    onUpdateClick = {
                        dogToUpdate = dog
                    },
                    onDeleteClick = {
                        dogToDelete = dog
                    })
            }
        }

        if(showCheckedInDialog) {
            ListCheckedInDogs(
                dogs = state.dogs,
                onDismiss = { showCheckedInDialog = false }
            )
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

        dogToUpdate?.let { dog ->
            AddDog(
                dogToUpdate = dog,
                onDismiss = { dogToUpdate = null },
                onConfirm = { name, age, breed, info ->
                    viewModel.updateDog(dog.id, name, age, breed, info)
                    dogToUpdate = null
                }
            )
        }

        dogToDelete?.let { dog ->
            DeleteDog(
                dogName = dog.name,
                onDismiss = { dogToDelete = null },
                onConfirm = {
                    viewModel.removeDog(dog.id)
                    dogToDelete = null
                }
            )
        }

        selectedDog?.let { dog ->
            ViewDogInfo(
                dog = dog,
                onDismiss = { selectedDog = null }
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