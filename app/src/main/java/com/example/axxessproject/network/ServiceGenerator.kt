package com.example.axxessproject.network

import com.example.axxessproject.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * This class provides retrofit api instance. It also builds retrofit instance. All the code related
 * to retrofit must be kept here.
 */
object ServiceGenerator {
    private val retrofitBuilder = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    private val retrofit = retrofitBuilder.build()
    @JvmStatic
     public val imageApi = retrofit.create(ImageApi::class.java)
}