package com.thoriq.plantsnap.view.result

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
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

        viewModel.getPlant("rose")

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

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}