package com.project.topengbali.ui.result

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.project.topengbali.R
import com.project.topengbali.ViewModelFactory
import com.project.topengbali.data.response.Topeng
import com.project.topengbali.databinding.ActivityResultBinding

//menampilkan hasil dari response dari api. logic ke xml
class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private var selectedImageUri: Uri? = null
    private val list = ArrayList<Topeng>()
    private val viewModel: ResultViewModel by viewModels {
        ViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val predictResult = intent.getDoubleExtra(PREDICT, 0.0)
        println(predictResult)
        val uriImage = intent.getStringExtra(URI_IMAGE)
        selectedImageUri = Uri.parse(uriImage)
        val context = this@ResultActivity


        Glide.with(this)
            .load(Uri.parse(uriImage))
            .centerCrop()
            .placeholder(R.drawable.ic_load)
            .into(binding.ivResult)

        viewModel.getDetail(predictResult.toInt(), context).observe(context) {
            binding.tvMaskTitle.text = it.name
            binding.tvMaskDesc.text = it.desc

            binding.rvImgMask.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val resultAdapter = ResultAdapter(it.url)
            binding.rvImgMask.adapter = resultAdapter
        }
    }

    companion object {
        const val URI_IMAGE = "image_result"
        const val PREDICT = "prediction"
    }


}