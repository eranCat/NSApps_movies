package com.erank.nsappsmoviesapi.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.erank.nsappsmoviesapi.models.Movie

@Database(entities = [Movie::class], version = 1,exportSchema = false)
abstract class MoviesDataBase : RoomDatabase() {
    abstract fun movieDAO(): MovieDAO
}