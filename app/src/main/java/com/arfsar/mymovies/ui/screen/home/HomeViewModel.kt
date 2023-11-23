package com.arfsar.mymovies.ui.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arfsar.mymovies.data.FilmRepository
import com.arfsar.mymovies.model.FavoriteFilm
import com.arfsar.mymovies.model.Film
import com.arfsar.mymovies.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: FilmRepository
) : ViewModel(){
    private val _uiState: MutableStateFlow<UiState<List<FavoriteFilm>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<FavoriteFilm>>> get() = _uiState

    fun getAllFilm() {
        viewModelScope.launch {
            repository.getAllFilm()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { favoriteFilms ->
                    _uiState.value = UiState.Success(favoriteFilms)
                }
        }
    }

    fun toggleFavorite(film: Film, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.updateFavoriteFilm(film.id, isFavorite)
            getAllFilm()
        }
    }

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun search(newQuery: String) {
        _query.value = newQuery
        viewModelScope.launch {
            repository.searchFilm(_query.value)
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { searchFilms ->
                    _uiState.value = UiState.Success(searchFilms)
                }
        }
    }
}