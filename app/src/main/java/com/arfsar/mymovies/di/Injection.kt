package com.arfsar.mymovies.di

import com.arfsar.mymovies.data.FilmRepository

object Injection {
    fun provideRepository(): FilmRepository {
        return FilmRepository.getInstance()
    }
}