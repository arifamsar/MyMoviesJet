package com.arfsar.mymovies.model

data class Film(
    val id: Long,
    val image: Int,
    val title: String,
    val genre: String,
    val synopsis: String
)