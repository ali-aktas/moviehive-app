package com.aliaktas.cinematch.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliaktas.cinematch.data.model.Movie
import com.aliaktas.cinematch.data.repository.FavoriteMovieRepository
import com.aliaktas.cinematch.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val favoriteRepository: FavoriteMovieRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _movie = MutableStateFlow<Movie?>(null)
    val movie: StateFlow<Movie?> = _movie

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _trailerUrl = MutableStateFlow<String?>(null)
    val trailerUrl: StateFlow<String?> = _trailerUrl.asStateFlow()

    private val movieId: Int = checkNotNull(savedStateHandle.get<Int>("movieId"))

    init {
        loadMovieDetails(movieId)
        loadTrailer()
    }

    private fun loadTrailer() {
        viewModelScope.launch {
            try {
                val trailerKey = movieRepository.getMovieTrailer(movieId)
                _trailerUrl.value = if (trailerKey != null) {
                    "https://www.youtube.com/watch?v=$trailerKey"
                } else null
            } catch (e: Exception) {
                _trailerUrl.value = null
            }
        }
    }

    fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // First control by Room DB
                favoriteRepository.getFavoriteById(movieId)?.let { favoriteMovie ->
                    _movie.value = Movie(
                        id = favoriteMovie.id,
                        title = favoriteMovie.title,
                        overview = favoriteMovie.overview,
                        posterPath = favoriteMovie.posterPath,
                        releaseDate = favoriteMovie.releaseDate,
                        voteAverage = favoriteMovie.voteAverage
                    )
                    _isLoading.value = false
                    return@launch
                }

                // Get details by API if that is not in Room DB
                val movieDetails = movieRepository.getMovieDetails(movieId)
                _movie.value = movieDetails
            } catch (e: Exception) {
                _error.value = "Could not load movie details. Please check your internet connection."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun isFavorite(movieId: Int): Flow<Boolean> {
        return favoriteRepository.isFavorite(movieId)
    }

    fun toggleFavorite() {
        val currentMovie = _movie.value ?: return
        viewModelScope.launch {
            val isFavorite = favoriteRepository.isFavorite(currentMovie.id).first()
            if (isFavorite) {
                favoriteRepository.removeFromFavorites(currentMovie)
            } else {
                favoriteRepository.addToFavorites(currentMovie)
            }
        }
    }
}