package com.thoriq.plantsnap.view.signup

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.thoriq.plantsnap.databinding.ActivitySignupBinding
import com.thoriq.plantsnap.view.ViewModelFactory
import com.thoriq.plantsnap.view.main.MainActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        val factory = ViewModelFactory.getInstance(application)
        signupViewModel = ViewModelProvider(this, factory).get(SignupViewModel::class.java)

        signupViewModel.isLoading.observe(this){
            showLoading(it)
        }

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val pass = binding.passwordEditText.text.toString()

            if(email.isNotEmpty() && name.isNotEmpty() && pass.isNotEmpty()){
                signupViewModel.signUp(this, name, email, pass)
            } else{
                Toast.makeText(this, "Kolom Kosong Tidak Diizinkan !!", Toast.LENGTH_SHORT).show()
            }
            signupViewModel.isRegister.observe(this){
                if (it){
//                    AlertDialog.Builder(this).apply {
//                        setTitle("Yeah!")
//                        setMessage("Anda berhasil registrasi")
//                        setPositiveButton("Lanjut") { _, _ ->
//                            val intent = Intent(context, MainActivity::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                            startActivity(intent)
//                            finish()
//                        }
//                        create()
//                        show()
//                    }
                    Toast.makeText(this, "Registrasi Berhasil <3 ", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            }

        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}