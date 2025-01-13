package com.example.educationalapp.network

data class QuoteResponse(
    val contents: Contents
)

data class Contents(
    val quotes: List<Quote>
)

data class Quote(
    val quote: String,
    val author: String
)