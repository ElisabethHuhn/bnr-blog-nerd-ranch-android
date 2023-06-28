package com.bignerdranch.android.blognerdranch.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {

    private const val baseUrl = "http://10.0.2.2:8106/"  // "localhost" is the emulator's host. 10.0.2.2 goes to your computer

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            //define where to get the Json
            .baseUrl(baseUrl)
            // convert JSON object to Kotlin object
            .addConverterFactory(GsonConverterFactory.create())
            //build the Retrofit instance
            .build()
    }
}