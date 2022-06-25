package com.project.topengbali

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.project.topengbali.ui.about.AboutActivity
import com.project.topengbali.ui.camera.CameraXActivity
import com.project.topengbali.databinding.ActivityMainBinding
import com.project.topengbali.ui.camera.PreviewActivity
import com.project.topengbali.ui.guide.GuideActivity
import com.project.topengbali.ui.result.ResultActivity
import com.project.topengbali.utils.rotateBitmap
import com.project.topengbali.utils.uriToFile
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var getFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnCamera.setOnClickListener { startCamera() }
            btnGallery.setOnClickListener { startGallery() }
            btnGuide.setOnClickListener { startGuide() }
            btnAbout.setOnClickListener { startAbout() }
            btnSetting.setOnClickListener { startSetting() }
            btnOut.setOnClickListener { finishAffinity() }
        }
    }

    private fun startCamera() {
        val intent = Intent(this, CameraXActivity::class.java)
        startActivity(intent)
//        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startGuide() {
        startActivity(Intent(this@MainActivity, GuideActivity::class.java))
    }

    private fun startAbout() {
        startActivity(Intent(this@MainActivity, AboutActivity::class.java))
    }

    private fun startSetting() {
        val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
        startActivity(mIntent)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@MainActivity)
            getFile = myFile
            val nextIntent = Intent(this@MainActivity, PreviewActivity::class.java)
            nextIntent.putExtra(
                PreviewActivity.CAMERA_X_RESULT,
                selectedImg.toString()
            )
            startActivity(nextIntent)
        }
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finishAffinity()
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}