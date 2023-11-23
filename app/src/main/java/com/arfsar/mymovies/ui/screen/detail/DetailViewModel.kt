package com.arfsar.mymovies.ui.screen.detail

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfsar.mymovies.data.FilmRepository
import com.arfsar.mymovies.model.FavoriteFilm
import com.arfsar.mymovies.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: FilmRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<FavoriteFilm>> =
        MutableStateFlow(UiState.Loading)
    val uiState: MutableStateFlow<UiState<FavoriteFilm>>
        get() = _uiState

    fun getFilmById(filmId: Long) {
        _uiState.value = UiState.Loading
        _uiState.value = UiState.Success(repository.getFilmById(filmId))
    }

    fun setFavoriteFilm(film: FavoriteFilm) {
        viewModelScope.launch {
            film.isFavoriteFilm = !film.isFavoriteFilm
            repository.updateFavoriteFilm(film.film.id, film.isFavoriteFilm)
        }
    }

}