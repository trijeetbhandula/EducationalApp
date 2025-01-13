package com.example.educationalapp.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface QuoteApiService {
    @Headers("Authorization: Bearer 9L0Z74Xih7RcczrNntfh3sfJpRXFA7XzQYXgrZb8")
    @GET("qod")
    fun getQuoteOfTheDay(): Call<QuoteResponse>
}