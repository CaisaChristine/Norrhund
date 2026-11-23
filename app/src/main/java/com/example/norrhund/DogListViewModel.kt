package com.example.norrhund

import android.widget.CheckedTextView
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

data class DogListUiState(
    val dogs: List<Dog> = startDogs
)

class DogListViewModel: ViewModel() {
    var uiState by mutableStateOf(DogListUiState())
        private set

    fun addDog(name: String, age: Int, breed: String, info: String) {
        val nextId = (uiState.dogs.maxOfOrNull { it.id } ?: 0) + 1
        val newDog = Dog(
            id = nextId,
            name = name,
            age = age,
            breed = breed,
            info = info.ifBlank { null },
            checkedIn = false
        )
        uiState = uiState.copy(dogs = uiState.dogs + newDog)
    }

    fun toggleCheckedIn(dogId: Int, isChecked: Boolean) {
        val updatedDog = uiState.dogs.map { dog ->
            if (dog.id == dogId) {
                dog.copy(checkedIn = isChecked)
            } else {
                dog
            }
        }

        uiState = uiState.copy(dogs = updatedDog)
    }
}