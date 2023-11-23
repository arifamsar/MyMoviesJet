package com.arfsar.mymovies.data

import com.arfsar.mymovies.model.FakeFilmDataSource
import com.arfsar.mymovies.model.FavoriteFilm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FilmRepository {

    private val favoriteFilm = mutableListOf<FavoriteFilm>()

    init {
        if (favoriteFilm.isEmpty()) {
            FakeFilmDataSource.dummyFilm.forEach {
                favoriteFilm.add(FavoriteFilm(it, false))
            }
        }
    }

    fun getAllFilm(): Flow<List<FavoriteFilm>> {
        return flowOf(favoriteFilm)
    }

    fun getFilmById(filmId: Long): FavoriteFilm {
        return favoriteFilm.first {
            it.film.id == filmId
        }
    }

    fun updateFavoriteFilm(filmId: Long, newFavoriteFilmValue: Boolean): Flow<Boolean> {
        val index = favoriteFilm.indexOfFirst { it.film.id == filmId }
        val result = if (index >= 0) {
            val favoriteFilm = favoriteFilm[index]
            favoriteFilm.isFavoriteFilm = newFavoriteFilmValue
            true
        } else {
            false
        }
        return flowOf(result)
    }

    fun getAddedFavoriteFilm(): Flow<List<FavoriteFilm>> {
        return getAllFilm()
            .map { favoriteFilm ->
                favoriteFilm.filter { favoriteFilm ->
                    favoriteFilm.isFavoriteFilm
                }
            }
    }

    fun searchFilm(query: String): Flow<List<FavoriteFilm>> {
        return getAllFilm()
            .map { film ->
                film.filter {
                    it.film.title.contains(query, ignoreCase = true)
                }
            }
    }

    companion object {
        @Volatile
        private var instance: FilmRepository? = null

        fun getInstance(): FilmRepository =
            instance ?: synchronized(this) {
                FilmRepository().apply {
                    instance = this
                }
            }
    }
}