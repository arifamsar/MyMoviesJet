package com.arfsar.mymovies.ui.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Favorite: Screen("favorite")
    object Profile: Screen("profile")
    object DetailFilm: Screen("home/{filmId}") {
        fun createRoute(filmId: Long) = "home/$filmId"
    }
}