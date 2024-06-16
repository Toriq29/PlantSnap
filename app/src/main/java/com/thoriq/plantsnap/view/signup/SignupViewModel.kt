package com.thoriq.plantsnap.view.signup

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.thoriq.plantsnap.data.UserRepository
import com.thoriq.plantsnap.data.pref.UserModel
import com.thoriq.plantsnap.data.remote.response.ErrorResponse
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
                    Log.d("UUID :", it.result.user?.uid ?: "uuid")
                    saveSession(UserModel(it.result.user?.uid ?: "uuid", true))

                    val db = Firebase.firestore

                    val user = hashMapOf(
                        "name" to name
                    )

                    it.result.user?.uid?.let { uuid ->
                        db.collection("users").document(
                            uuid
                        ).set(user)
                    }
                    _isLoading.value = false
                    _isRegister.value = true
                } else{
                    Toast.makeText(context, it.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                    _isLoading.value = false
                }

            }

//            try {
//                _isLoading.value = true
//                val response = userRepository.register(email, pass)
//                if (response.isSuccessful) {
//                    _isLoading.value = false
//                    val token = response.body()?.token
//                    val user = token?.let { UserModel(token, true) }
//
//                    user?.let { saveSession(it) }
//                    _isRegister.value = true
//
//                } else {
//                    _isLoading.value = false
//                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
//                    val errorMessage = Gson().fromJson(errorBody, ErrorResponse::class.java).error
//                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//                }
//            } catch (e: Exception) {
//                _isLoading.value = false
//                Toast.makeText(context, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
        }
    }
}