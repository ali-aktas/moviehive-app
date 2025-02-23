package com.aliaktas.cinematch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliaktas.cinematch.data.model.Genre
import com.aliaktas.cinematch.data.model.Movie
import com.aliaktas.cinematch.data.repository.MovieRepository
import com.google.gson.internal.GsonBuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _popularMovies = MutableStateFlow<List<Movie>>(emptyList())
    val popularMovies: StateFlow<List<Movie>> = _popularMovies

    private val _topRatedMovies = MutableStateFlow<List<Movie>>(emptyList())
    val topRatedMovies: StateFlow<List<Movie>> = _topRatedMovies

    private val _nowPlayingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val nowPlayingMovies: StateFlow<List<Movie>> = _nowPlayingMovies



    // Yeni eklenen state'ler
    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres

    private val _selectedGenreMovies = MutableStateFlow<List<Movie>>(emptyList())
    val selectedGenreMovies: StateFlow<List<Movie>> = _selectedGenreMovies

    private var selectedGenreId: Int? = null
    private var selectedGenrePage = 1
    //buraya kadar, çalışırsa düzenle!


    private var popularMoviesPage = 1
    private var topRatedMoviesPage = 1
    private var nowPlayingMoviesPage = 1

    init {
        getPopularMovies()
        getTopRatedMovies()
        getNowPlayingMovies()
        getGenres()
    }

    private fun getPopularMovies() {
        viewModelScope.launch {
            try {
                val response = repository.getPopularMovies(popularMoviesPage)
                _popularMovies.value += response.results // Mevcut listeye yeni filmleri ekle
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getTopRatedMovies() {
        viewModelScope.launch {
            try {
                val response = repository.getTopRatedMovies(topRatedMoviesPage)
                _topRatedMovies.value += response.results // Mevcut listeye yeni filmleri ekle
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getNowPlayingMovies() {
        viewModelScope.launch {
            try {
                val response = repository.getNowPlayingMovies(nowPlayingMoviesPage)
                _nowPlayingMovies.value += response.results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getGenres() {
        viewModelScope.launch {
            try {
                val response = repository.getMovieGenres()
                _genres.value = response.genres
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun selectGenre(genreId: Int) {
        if (selectedGenreId != genreId) {
            selectedGenreId = genreId
            selectedGenrePage = 1
            _selectedGenreMovies.value = emptyList()
            getMoviesByGenre()
        }
    }

    private fun getMoviesByGenre() {
        selectedGenreId?.let { genreId ->
            viewModelScope.launch {
                try {
                    val response = repository.getMoviesByGenre(genreId, selectedGenrePage)
                    _selectedGenreMovies.value += response.results
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun loadNextPage(category: String) {
        when (category) {
            "genre" -> {
                if (selectedGenreId != null) {
                    selectedGenrePage++
                    getMoviesByGenre()
                }
            }
            else -> {
                when (category) {
                    "popular" -> {
                        popularMoviesPage++
                        getPopularMovies()
                    }
                    "top_rated" -> {
                        topRatedMoviesPage++
                        getTopRatedMovies()
                    }
                    "now_playing" -> {
                        nowPlayingMoviesPage++
                        getNowPlayingMovies()
                    }
                }
            }
        }
    }

}