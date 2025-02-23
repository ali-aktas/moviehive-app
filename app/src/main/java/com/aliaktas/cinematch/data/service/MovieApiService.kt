package com.aliaktas.cinematch.data.service

import com.aliaktas.cinematch.data.model.GenreResponse
import com.aliaktas.cinematch.data.model.Movie
import com.aliaktas.cinematch.data.model.MovieResponse
import com.aliaktas.cinematch.data.model.VideoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    // Get popular movies
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1, // Sayfa numarası dinamik olacak
        @Query("release_date.gte") releaseDateGte: String? = null
    ): MovieResponse

    // Get top-rated movies
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1 // Sayfa numarası dinamik olacak
    ): MovieResponse

    // Get Now Playing Movies
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("release_date.gte") releaseDateGte: String? = null
    ): MovieResponse

    // Movie Details
    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): Movie

    // Search movies
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): MovieResponse

    // Yeni eklenen endpoint
    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "en-US"
    ): GenreResponse

    // Kategori bazlı film listesi için yeni endpoint
    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: Int,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): MovieResponse

    //Fragment endpoint
    @GET("movie/{movieId}/videos")
    suspend fun getMovieVideos(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String
    ): VideoResponse

}