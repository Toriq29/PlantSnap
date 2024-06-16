package com.thoriq.plantsnap.view.result

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.thoriq.plantsnap.data.UserRepository
import com.thoriq.plantsnap.data.pref.Plant
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ResultViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _plant = MutableLiveData<Plant>()
    val plant: LiveData<Plant> = _plant

    fun getPlant(namePlant: String){
        viewModelScope.launch {
            val db = Firebase.firestore
            _isLoading.value = true

            val docRef = db.collection("plant").document(namePlant)

            val token = repository.getSession().first().token

            val data = hashMapOf(
                "name" to namePlant
            )


            val historyRef = db.collection("users")
                .document(token)
                .collection("history")

            // Query to check if the data already exists
            historyRef.whereEqualTo("name", data["name"])
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        // Data does not exist, so add it
                        historyRef.add(data).addOnSuccessListener {
                            Log.d(TAG, "success $namePlant add data to history")
                        }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }
                    } else {
                        // Data already exists
                        Log.d(TAG, "Data already exists in the history")
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error checking existing data", e)
                }

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        _isLoading.value = false
                        val data = document.data!!



                        val plant = Plant(
                            name = data["Name"] as String,
                            temperature = data["Temprature"] as String,
                            sunlight = data["Sunlight"] as String,
                            water = data["Water"] as String,
                            fertilizing = data["Fertilizing"] as String,
                            repotting = data["Repotting"] as String,
                            pests = data["Pests"] as String,
                            description = data["Description"] as String

                        )

                        _plant.value = plant
                    } else {
                        _isLoading.value = false
                        Log.d(ContentValues.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    _isLoading.value = false
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
        }

    }

}