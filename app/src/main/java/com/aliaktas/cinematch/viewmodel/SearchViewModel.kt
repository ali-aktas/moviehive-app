package com.aliaktas.cinematch.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliaktas.cinematch.data.model.Movie
import com.aliaktas.cinematch.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList())
    val searchResults: StateFlow<List<Movie>> = _searchResults

    // Loading state'ini ekleyelim
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    private var searchJob: Job? = null

    fun searchMovies(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _isLoading.value = true
            delay(300) // Debounce

            try {
                Log.d("SearchViewModel", "Searching for: $query")  // 🔍 Log ekledik
                val response = repository.searchMovies(query)
                Log.d("SearchViewModel", "Search result count: ${response.results.size}") // Sonuç sayısını logla
                _searchResults.value = response.results
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error: ${e.message}") // Hata logu
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

}