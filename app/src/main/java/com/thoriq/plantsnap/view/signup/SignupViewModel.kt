package com.thoriq.plantsnap.view.signup

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.thoriq.plantsnap.data.UserRepository
import com.thoriq.plantsnap.data.pref.UserModel
import kotlinx.coroutines.launch

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isRegister = MutableLiveData<Boolean>()
    val isRegister: LiveData<Boolean> = _isRegister

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }

    fun signUp(context : Context, name: String, email: String, pass : String){
        viewModelScope.launch {
            val firebaseAuth = FirebaseAuth.getInstance()
            _isLoading.value = true
            firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                if (it.isSuccessful){
                    saveSession(UserModel(email, true))
                    _isLoading.value = false
                    _isRegister.value = true
                } else{
                    Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    _isLoading.value = false
                }

            }
        }
    }
}