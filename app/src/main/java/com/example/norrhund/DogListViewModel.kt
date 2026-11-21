package com.example.norrhund

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

data class DogListUiState(
    val dogs: List<Dog> = startDogs,
)

class DogListViewModel: ViewModel() {
    var uiState by mutableStateOf(DogListUiState())
        private set
}