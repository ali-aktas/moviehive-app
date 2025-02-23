package com.aliaktas.cinematch.data.repository

import com.aliaktas.cinematch.BuildConfig
import com.aliaktas.cinematch.BuildConfig.TMDB_API_KEY
import com.aliaktas.cinematch.data.model.GenreResponse
import com.aliaktas.cinematch.data.model.Movie
import com.aliaktas.cinematch.data.model.MovieResponse
import com.aliaktas.cinematch.data.service.MovieApiService
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiService: MovieApiService
) {

    // Get Popular Movies (son 6 aydaki popüler filmler)
    suspend fun getPopularMovies(page: Int): MovieResponse {
        // Calculate the last 6 months
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -6)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sixMonthsAgo = dateFormat.format(calendar.time)

        // Get the popular movies of last 6 months from API
        return apiService.getPopularMovies(
            apiKey = BuildConfig.TMDB_API_KEY,
            page = page,
            releaseDateGte = sixMonthsAgo
        )
    }

    // Get Top Rated Movies
    suspend fun getTopRatedMovies(page: Int): MovieResponse {
        return apiService.getTopRatedMovies(
            apiKey = BuildConfig.TMDB_API_KEY,
            page = page
        )
    }

    suspend fun getNowPlayingMovies(page: Int): MovieResponse {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val lastOneMonth = dateFormat.format(calendar.time)

        return apiService.getNowPlayingMovies(
            apiKey = BuildConfig.TMDB_API_KEY,
            page = page,
            releaseDateGte = lastOneMonth
        )
    }

    // Get Movie Details
    suspend fun getMovieDetails(movieId: Int): Movie {
        return apiService.getMovieDetails(
            movieId = movieId,
            apiKey = BuildConfig.TMDB_API_KEY
        )
    }

    // Search Movies
    suspend fun searchMovies(query: String): MovieResponse {
        return apiService.searchMovies(
            apiKey = BuildConfig.TMDB_API_KEY,
            query = query
        )
    }

    suspend fun getMovieGenres(): GenreResponse {
        return apiService.getMovieGenres(
            apiKey = BuildConfig.TMDB_API_KEY
        )
    }

    suspend fun getMoviesByGenre(genreId: Int, page: Int): MovieResponse {
        return apiService.getMoviesByGenre(
            apiKey = BuildConfig.TMDB_API_KEY,
            genreId = genreId,
            page = page
        )
    }

    suspend fun getMovieTrailer(movieId: Int): String? {
        return try {
            val response = apiService.getMovieVideos(movieId, TMDB_API_KEY)
            // Filter only Youtube videos
            response.results.find { video ->
                video.site.equals("YouTube", ignoreCase = true) &&
                        video.type.equals("Trailer", ignoreCase = true)
            }?.key
        } catch (e: Exception) {
            null
        }
    }

}