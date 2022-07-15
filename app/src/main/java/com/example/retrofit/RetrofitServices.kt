package com.example.retrofit

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitServices {
    @GET("json")
    fun getCurrencies(): Call<List<CurrencyRetrofit>>
}