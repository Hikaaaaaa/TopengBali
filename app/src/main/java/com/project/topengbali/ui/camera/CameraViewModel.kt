package com.project.topengbali.ui.camera

import android.content.Context
import android.text.BoringLayout
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.topengbali.data.remote.ApiConfig
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CameraViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun postImage(
        imageMultipart: MultipartBody.Part,
        context: Context
    ): LiveData<Double>{
        _isLoading.postValue(true)
        val result = MutableLiveData<Double>()
        val service = ApiConfig.getApiService().getClassification(imageMultipart)

        service.enqueue(object : Callback<Double>{
            override fun onResponse(
                call: Call<Double>,
                response: Response<Double>
            ) {
                if (response.isSuccessful) {
                    _isLoading.postValue(false)
                    val responseBody = response.body()
                        result.value = responseBody!!
                        Toast.makeText(context, "$responseBody", Toast.LENGTH_SHORT).show()

                } else {
                    _isLoading.postValue(false)
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<Double>, t: Throwable) {
                _isLoading.postValue(false)
                Toast.makeText(
                    context,
                    "Gagal instance Retrofit",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
        return result
    }
}