package com.thoriq.plantsnap.view.analyze

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thoriq.plantsnap.data.UserRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AnalyzeViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isUpload = MutableLiveData<Boolean>()
    val isUpload: LiveData<Boolean> = _isUpload

    private val _result = MutableLiveData<String?>()
    val result: LiveData<String?> = _result

    fun uploadImage(context: Context, imageUri: Uri) {
        _isLoading.value = true
        imageUri.let { uri ->
            try {
                val imageFile = uriToFile(uri, context).reduceFileImage()
                Log.d("Image File", "showImage: ${imageFile.path}")

                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "image",
                    imageFile.name,
                    requestImageFile
                )

                viewModelScope.launch {
                    try {
                        val response = repository.upload(multipartBody)
                        val label = response.body()?.label
                        if (label != null) {
                            _result.value = label
                            _isUpload.value = true
                        } else {
                            // Handle the case where the label is null
                            Log.e("Upload Image", "No label found in the response")
                            _result.value = "No label found"
                            _isUpload.value = false
                        }
                    } catch (e: Exception) {
                        // Handle any exceptions that may occur during the upload
                        Log.e("Upload Image", "Upload failed", e)
                        _result.value = "Upload failed: ${e.message}"
                        _isUpload.value = false
                    } finally {
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                Log.e("Upload Image", "Error processing image file", e)
                _result.value = "Error processing image file: ${e.message}"
                _isUpload.value = false
                _isLoading.value = false
            }
        }
    }
}