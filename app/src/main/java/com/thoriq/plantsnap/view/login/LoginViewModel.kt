package com.thoriq.plantsnap.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thoriq.plantsnap.data.UserRepository
import com.thoriq.plantsnap.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}