package com.project.topengbali.data.remote

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("classification")
    fun getClassification(
        @Part image: MultipartBody.Part
    ): Call<Double>
}