package com.example.iiph.ui.resume

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ResumeBuilderViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResumeBuilderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResumeBuilderViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}