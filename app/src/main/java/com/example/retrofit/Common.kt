package com.example.retrofit

object Common {
    const val BASE_URL = "https://cbu.uz/uz/arkhiv-kursov-valyut/"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getRetrofit(BASE_URL).create(RetrofitServices::class.java)
}