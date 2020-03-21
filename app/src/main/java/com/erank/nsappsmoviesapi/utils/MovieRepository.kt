package com.erank.nsappsmoviesapi.utils

import android.content.Context
import androidx.room.Room
import com.erank.nsappsmoviesapi.models.Movie
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch

class MovieRepository private constructor(context: Context) {

    private val dBName = "moviesDB"

    private val moviesDataBase: MoviesDataBase

    var movies: List<Movie>

    companion object {
        private var INSTANCE: MovieRepository? = null
        fun getInstance(context: Context? = null) =
            context?.let {
                INSTANCE ?: MovieRepository(context).also {
                    INSTANCE = it
                }
            } ?: INSTANCE ?: throw RepositoryNeedsContextAtLeastOnceException()
    }

    init {
        moviesDataBase = buildDataBase(context)
        movies = emptyList()
    }

    private fun buildDataBase(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            MoviesDataBase::class.java, dBName
        ).fallbackToDestructiveMigration().build()


    fun insertAll(movies: List<Movie>, done: (() -> Unit)? = null) {
        GlobalScope.launch {
            moviesDataBase.movieDAO().insertAll(movies)
            this@MovieRepository.movies = movies
            done?.invoke()
        }
    }

    fun insert(movie: Movie, done: (()->Unit)? = null) {
        GlobalScope.launch {
            moviesDataBase.movieDAO().insert(movie)
            this@MovieRepository.movies += movie
            done?.invoke()
        }
    }

    fun getAllByYear(callback: OnDataFetchedListener<List<Movie>>) {
        GlobalScope.launch {
            moviesDataBase.movieDAO().getAllMoviesSortedByYear().let {
                movies = it
                callback.onDataFetched(it)
            }
        }
    }


    private fun getMovie(movie: Movie, callback: (Movie?)->Unit) {
        GlobalScope.launch {
            moviesDataBase.movieDAO().getMovieByTitle(movie.title).let {
                callback(it)
            }
        }
    }

    fun addIfNotExist(movie: Movie, done: (wasAdded:Boolean) -> Unit) {
        getMovie(movie) { movieFromDB ->

            if (movieFromDB != null) {//movie exists in DB
                done(false)
                return@getMovie
            }

            insert(movie){
                done(true)
            }
        }
    }

    class RepositoryNeedsContextAtLeastOnceException : RuntimeException()

    interface OnDataFetchedListener<T> {
        fun onDataFetched(data: T)
    }
}
