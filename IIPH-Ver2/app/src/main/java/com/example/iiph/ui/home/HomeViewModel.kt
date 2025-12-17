package com.example.iiph.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.iiph.data.SessionManager
import com.example.iiph.model.Listing
import com.example.iiph.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val listings: List<Listing>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        fetchListings()
    }

    private fun fetchListings() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val response = RetrofitInstance.api.getListings()
                if (response.isSuccessful && response.body() != null) {
                    val allListings = response.body()!!
                    val currentYear = sessionManager.getCurrentYear()

                    val filteredListings = when (currentYear) {
                        1, 2, 3 -> allListings.filter { it.type.equals("internship", ignoreCase = true) }
                        4 -> allListings.filter { it.type.equals("job", ignoreCase = true) }
                        else -> emptyList() // Or show all if you prefer
                    }
                    _uiState.value = HomeUiState.Success(filteredListings)
                } else {
                    _uiState.value = HomeUiState.Error("Failed to load listings")
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}