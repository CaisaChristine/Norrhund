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

    private var nextId = (uiState.dogs.maxOfOrNull { it.id } ?: 0) + 1

    fun addDog(name: String, age: Int, breed: String, info: String) {
        val newDog = Dog(
            id = nextId++,
            name = name,
            age = age,
            breed = breed,
            info = info.ifBlank { null },
            checkedIn = false
        )
        uiState = uiState.copy(dogs = uiState.dogs + newDog)
    }

    fun updateDog(id:Int, name: String, age: Int, breed: String, info: String) {
        val updatedDogs = uiState.dogs.map { dog ->
            if(dog.id == id) {
                dog.copy(
                    name = name,
                    age = age,
                    breed = breed,
                    info = info.ifBlank { null }
                )
            } else {
                dog
            }
        }
        uiState = uiState.copy(dogs = updatedDogs)
    }

    fun removeDog(dogId: Int) {
        uiState = uiState.copy(
            dogs = uiState.dogs.filter { it.id != dogId }
        )
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