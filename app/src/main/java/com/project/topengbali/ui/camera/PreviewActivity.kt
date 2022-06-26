package com.project.topengbali.ui.camera

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.project.topengbali.R
import com.project.topengbali.ViewModelFactory
import com.project.topengbali.databinding.ActivityPreviewBinding
import com.project.topengbali.ui.result.ResultActivity
import com.project.topengbali.ui.result.ResultActivity.Companion.PREDICT
import com.project.topengbali.ui.result.ResultActivity.Companion.URI_IMAGE
import com.project.topengbali.utils.reduceFileImage
import com.project.topengbali.utils.rotateBitmap
import com.project.topengbali.utils.uriToFile
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

//compress image, pemanggilan prosess pengiriman image ke server
class PreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewBinding
    private var selectedImageUri: Uri? = null
    private val viewModel: CameraViewModel by viewModels {
        ViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uriImage = intent.getStringExtra(CAMERA_X_RESULT)
        selectedImageUri = Uri.parse(uriImage)

        Glide.with(this)
            .load(selectedImageUri)
            .centerCrop()
            .placeholder(R.drawable.ic_load)
            .into(binding.ivPreview)

        binding.btnClose.setOnClickListener {
            finish()
        }
        binding.btnNext.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {
        if (selectedImageUri != null){
            val context = this@PreviewActivity
            val image = uriToFile(selectedImageUri!!, context)
//            val reducedImage = reduceFileImage(image)

            lifecycleScope.launch(Dispatchers.IO) {
                /**
                 * Enter Coroutine Scope, to compress image
                 */
//                val fileImage = getFile(this@PreviewActivity, selectedPhotoUri)
                val compressedImageFile = Compressor.compress(this@PreviewActivity, image) // FIXME: 23/02/2022 Beware of using bang operator

                /**
                 * Enter Main Thread, to access view
                 */
                withContext(Dispatchers.Main) {
                    updateImage(compressedImageFile)
                }
            }

//            val requestImageFile = reducedImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
//            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
//                "image",
//                reducedImage.name,
//                requestImageFile
//            )
            viewModel.isLoading.observe(context){
                binding.progressCircular.visibility = if (it) View.VISIBLE else View.GONE
                binding.btnNext.isEnabled = !it
                binding.btnClose.isEnabled = !it
            }

        }
    }
    private fun updateImage(fileImage: File?){
        val context = this@PreviewActivity
//        val requestImageFile = reducedImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            fileImage?.name,
            fileImage!!.asRequestBody("image/jpeg".toMediaTypeOrNull())
        )

        viewModel.postImage(imageMultipart, context).observe(context){
            val nextIntent =
                Intent(this@PreviewActivity, ResultActivity::class.java)
            nextIntent.putExtra(PREDICT, it)
            nextIntent.putExtra(URI_IMAGE, selectedImageUri.toString())
            startActivity(nextIntent)
            finish()
        }
    }

    companion object {
        const val CAMERA_X_RESULT = "200"
    }
}