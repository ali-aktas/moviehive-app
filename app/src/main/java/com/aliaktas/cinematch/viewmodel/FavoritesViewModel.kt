package com.aliaktas.cinematch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliaktas.cinematch.data.model.Movie
import com.aliaktas.cinematch.data.repository.FavoriteMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoriteMovieRepository
) : ViewModel() {

    // Favori filmleri StateFlow olarak tut
    val favorites: StateFlow<List<Movie>> = repository.getAllFavorites()
        .map { favoriteMovies ->
            favoriteMovies.map { favoriteMovie ->
                Movie(
                    id = favoriteMovie.id,
                    title = favoriteMovie.title,
                    overview = favoriteMovie.overview,
                    posterPath = favoriteMovie.posterPath,
                    releaseDate = favoriteMovie.releaseDate,
                    voteAverage = favoriteMovie.voteAverage
                )
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    // Filmi favorilere ekle
    fun addToFavorites(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addToFavorites(movie)
        }
    }

    // Filmi favorilerden kaldır
    fun removeFromFavorites(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeFromFavorites(movie)
        }
    }
}
