package com.example.interviewbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class InterviewViewModelFactory(private val dao: SessionDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InterviewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InterviewViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}