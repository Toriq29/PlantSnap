package com.thoriq.plantsnap.view.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.thoriq.plantsnap.R
import com.thoriq.plantsnap.data.pref.History
import com.thoriq.plantsnap.data.pref.Plant
import com.thoriq.plantsnap.databinding.ActivityMainBinding
import com.thoriq.plantsnap.view.ViewModelFactory
import com.thoriq.plantsnap.view.analyze.AnalyzeActivity
import com.thoriq.plantsnap.view.analyze.getImageUri
import com.thoriq.plantsnap.view.recommendation.RecommendationActivity
import com.thoriq.plantsnap.view.result.ResultActivity
import com.thoriq.plantsnap.view.welcome.WelcomeActivity
import com.yalantis.ucrop.UCrop

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
            else{
                viewModel.getHistory()
            }
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu1 -> {
                    viewModel.logout()
                    true
                }
                else -> false
            }
        }

        viewModel.result.observe(this){ result ->
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra(ResultActivity.EXTRA_RESULT, result)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
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
        binding.resultButton.setOnClickListener{
//            startActivity(Intent(this, AnalyzeActivity::class.java))
            val options = arrayOf("Gallery", "Camera")
            AlertDialog.Builder(this).apply {
                setItems(options) {dialog, which ->
                    when (which) {
                        0 -> {
                            startGallery()
                        }
                        1 -> {
                            startCamera()
                        }
                    }
                }
                create()
                show()
            }
        }

        binding.recommendationButton.setOnClickListener{
            startActivity(Intent(this, RecommendationActivity::class.java))
        }
    }

    private fun setHistoryData(githubUsers: List<History>) {
        val adapter = HistoryAdapter()
        adapter.submitList(githubUsers)
        binding.rvHistory.adapter = adapter
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            UCrop.of(uri, Uri.fromFile(cacheDir.resolve("${System.currentTimeMillis()}.jpg")))
                .withAspectRatio(16F, 16F)
                .withMaxResultSize(1000, 1000)
                .start(this)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            currentImageUri = UCrop.getOutput(data!!)
            currentImageUri?.let {
                viewModel.uploadImage(this@MainActivity, it)
            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val errorMessage = UCrop.getError(data!!)?.message.toString()
            showToast(errorMessage)
            Log.e("Ucrop :", errorMessage)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        currentImageUri?.let {
            viewModel.uploadImage(this@MainActivity, it)
        } ?: run {
            showToast(getString(R.string.empty_image_warning))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}