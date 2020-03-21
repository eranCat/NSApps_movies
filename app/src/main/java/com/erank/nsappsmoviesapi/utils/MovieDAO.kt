package com.erank.nsappsmoviesapi.utils

import androidx.room.*
import com.erank.nsappsmoviesapi.models.Movie

@Dao
interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<Movie>)

    @Insert
    fun insert(movie: Movie)

    @Query("SELECT * FROM Movies ORDER BY releaseYear DESC")
    fun getAllMoviesSortedByYear(): List<Movie>

    @Query("DELETE FROM Movies")
    fun deleteAllMovies()

    @Query("SELECT * FROM Movies WHERE title=:title LIMIT 1")
    fun getMovieByTitle(title: String): Movie?
}