package com.example.iiph.ui.resume

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ResumeBuilderViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResumeBuilderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ResumeBuilderViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}