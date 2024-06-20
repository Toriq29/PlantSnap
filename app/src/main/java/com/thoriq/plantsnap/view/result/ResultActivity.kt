package com.thoriq.plantsnap.view.result

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.thoriq.plantsnap.R
import com.thoriq.plantsnap.databinding.ActivityLoginBinding
import com.thoriq.plantsnap.databinding.ActivityResultBinding
import com.thoriq.plantsnap.view.ViewModelFactory
import com.thoriq.plantsnap.view.login.LoginViewModel
import com.thoriq.plantsnap.view.main.MainViewModel

class ResultActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }

    private lateinit var binding: ActivityResultBinding

    private val viewModel by viewModels<ResultViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isLoading.observe(this){
            showLoading(it)
        }

        val result = intent.getStringExtra(EXTRA_RESULT)

        if (intent.hasExtra(EXTRA_IMAGE_URI)) {
            val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
            imageUri?.let {
                Log.d("Image URI", "showImage: $it")
                showResult(it)
            }
        }

        result?.let {
            showToast(it)
            viewModel.getPlant(it)
        }

        viewModel.plant.observe(this){ plant ->
            binding.name.text = plant.name
            binding.temprature.text = plant.temperature
            binding.sunlight.text = plant.sunlight
            binding.water.text = plant.water
            binding.fertilizing.text = plant.fertilizing
            binding.description.text = plant.description
            binding.repotting.text = plant.repotting
            binding.pests.text = plant.pests
        }
    }

    private fun showResult(imageUri: Uri) {
        binding.resultImage.setImageURI(imageUri)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}