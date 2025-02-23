package com.aliaktas.cinematch.data.repository

import com.aliaktas.cinematch.data.model.FavoriteMovie
import com.aliaktas.cinematch.data.local.FavoriteMovieDao
import com.aliaktas.cinematch.data.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteMovieRepository @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao
) {
    fun getAllFavorites(): Flow<List<FavoriteMovie>> = favoriteMovieDao.getAllFavorites()

    suspend fun addToFavorites(movie: Movie) {
        val favoriteMovie = FavoriteMovie(
            id = movie.id,
            title = movie.title,
            overview = movie.overview,
            posterPath = movie.posterPath,
            releaseDate = movie.releaseDate,
            voteAverage = movie.voteAverage
        )
        favoriteMovieDao.insertFavorite(favoriteMovie)
    }

    suspend fun removeFromFavorites(movie: Movie) {
        val favoriteMovie = FavoriteMovie(
            id = movie.id,
            title = movie.title,
            overview = movie.overview,
            posterPath = movie.posterPath,
            releaseDate = movie.releaseDate,
            voteAverage = movie.voteAverage
        )
        favoriteMovieDao.deleteFavorite(favoriteMovie)
    }

    fun isFavorite(movieId: Int): Flow<Boolean> = favoriteMovieDao.isFavorite(movieId)

    suspend fun getFavoriteById(movieId: Int): FavoriteMovie? = favoriteMovieDao.getFavoriteById(movieId)
}