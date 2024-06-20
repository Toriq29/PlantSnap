package com.thoriq.plantsnap.view.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.thoriq.plantsnap.data.UserRepository
import com.thoriq.plantsnap.data.pref.History
import com.thoriq.plantsnap.data.pref.UserModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _history = MutableLiveData<List<History>>()
    val history: LiveData<List<History>> = _history

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getHistory() {
        viewModelScope.launch {
            val db = Firebase.firestore
            _isLoading.value = true

            val token = repository.getSession().first().token

            val historyRef = db.collection("users")
                .document(token)
                .collection("history")

            historyRef.get()
                .addOnSuccessListener { documents ->
                    _isLoading.value = false
                    if (!documents.isEmpty) {
                        val historyList = documents.map { document ->
                            val data = document.data
                            History(
                                name = data["name"] as String
                            )
                        }
                        _history.value = historyList
                    } else {
                        Log.d(TAG, "No history found")
                    }
                }
                .addOnFailureListener { e ->
                    _isLoading.value = false
                    Log.w(TAG, "Error getting history", e)
                }

        }
    }

}