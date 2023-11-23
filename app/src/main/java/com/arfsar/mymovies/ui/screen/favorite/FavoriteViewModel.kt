package com.arfsar.mymovies.ui.screen.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfsar.mymovies.data.FilmRepository
import com.arfsar.mymovies.model.FavoriteFilm
import com.arfsar.mymovies.model.Film
import com.arfsar.mymovies.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.lang.Exception

class FavoriteViewModel(
    private val repository: FilmRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<FavoriteFilm>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<FavoriteFilm>>> get() = _uiState

    fun getAllFavoriteFilm() {
        viewModelScope.launch {
            try {
                val updatedFavoriteFilms = repository.getAddedFavoriteFilm().firstOrNull() ?: emptyList()
                _uiState.value = UiState.Success(updatedFavoriteFilms)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message.toString())
            }
        }
    }

    fun toggleFavoriteFilm(film: FavoriteFilm) {
        viewModelScope.launch {
            try {
                val updatedFilm = film.copy(isFavoriteFilm = !film.isFavoriteFilm)
                val isUpdated = repository.updateFavoriteFilm(updatedFilm.film.id, updatedFilm.isFavoriteFilm).first()
                if (isUpdated) {
                    getAllFavoriteFilm()
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message.toString())
            }
        }
    }
}