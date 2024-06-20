package com.thoriq.plantsnap.view.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.thoriq.plantsnap.data.UserRepository
import com.thoriq.plantsnap.data.pref.UserModel
import com.thoriq.plantsnap.data.remote.response.ErrorResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> = _isLogin

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(context : Context, email: String, pass : String){
        viewModelScope.launch {
//            val firebaseAuth = FirebaseAuth.getInstance()
//            _isLoading.value = true
//            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
//                if (it.isSuccessful){
//                    Log.d("UUID :", it.result.user?.uid ?: "uuid")
//                    saveSession(UserModel(it.result.user?.uid ?: "uuid", true))
//                    _isLoading.value = false
//                    _isLogin.value = true
//                } else{
//                    Toast.makeText(context, it.exception?.message.toString(), Toast.LENGTH_SHORT).show()
//                    _isLoading.value = false
//                }
//
//            }
            try {
                _isLoading.value = true
                val response = repository.login(email, pass)
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val user = UserModel(email, true)

                    user?.let { saveSession(it) }
                    _isLogin.value = true

                } else {
                    _isLoading.value = false
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    val errorMessage = Gson().fromJson(errorBody, ErrorResponse::class.java).error
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                _isLoading.value = false
                Toast.makeText(context, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}