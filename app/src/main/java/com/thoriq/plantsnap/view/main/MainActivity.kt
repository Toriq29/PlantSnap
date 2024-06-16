package com.thoriq.plantsnap.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.thoriq.plantsnap.R
import com.thoriq.plantsnap.data.pref.History
import com.thoriq.plantsnap.data.pref.Plant
import com.thoriq.plantsnap.databinding.ActivityMainBinding
import com.thoriq.plantsnap.view.ViewModelFactory
import com.thoriq.plantsnap.view.analyze.AnalyzeActivity
import com.thoriq.plantsnap.view.result.ResultActivity
import com.thoriq.plantsnap.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
            else{
                viewModel.getHistory()
            }
        }



        viewModel.isLoading.observe(this){
            showLoading(it)
        }

        viewModel.history.observe(this){
            setHistoryData(it)
        }



        setupView()
        setupAction()

        val layoutManager = LinearLayoutManager(this)
        binding.rvHistory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHistory.addItemDecoration(itemDecoration)




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

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
        binding.resultButton.setOnClickListener{
            startActivity(Intent(this, AnalyzeActivity::class.java))
        }
    }

    private fun setHistoryData(githubUsers: List<History>) {
        val adapter = HistoryAdapter()
        adapter.submitList(githubUsers)
        binding.rvHistory.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}