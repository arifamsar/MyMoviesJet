package com.arfsar.mymovies.model

data class FavoriteFilm(
    val film: Film,
    var isFavoriteFilm: Boolean = false
)
