package com.aliaktas.cinematch.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aliaktas.cinematch.data.model.FavoriteMovie

@Database(
    entities = [FavoriteMovie::class],
    version = 2,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao
}