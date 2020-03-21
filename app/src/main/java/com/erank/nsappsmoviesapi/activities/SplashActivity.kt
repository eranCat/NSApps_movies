package com.erank.nsappsmoviesapi.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.erank.nsappsmoviesapi.R
import com.erank.nsappsmoviesapi.models.Movie
import com.erank.nsappsmoviesapi.utils.DataSourceListener
import com.erank.nsappsmoviesapi.utils.JSONApiHelper
import com.erank.nsappsmoviesapi.utils.MovieRepository
import com.erank.nsappsmoviesapi.utils.isInternetAvailable
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashActivity : AppCompatActivity(), DataSourceListener,
    MovieRepository.OnDataFetchedListener<List<Movie>> {

    private val repo by lazy {MovieRepository.getInstance(applicationContext)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        repo.getAllByYear(this)
    }

    //    from local DB
    override fun onDataFetched(data: List<Movie>) {

        if (data.isNotEmpty()) {
            startActivity(Intent(this, MovieListActivity::class.java))
            return
        }

        if (!isInternetAvailable()) {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.noInternetConnection))
                .setMessage(getString(R.string.cantLoadMovies))
                .setPositiveButton("alright") { _, _ -> }
                .show()
            progressBar.visibility = View.GONE
            return
        }
        JSONApiHelper.loadMoviesFromJson(this)
    }

    //    from Json online
    override fun onMoviesLoaded(exception: Exception?, movies: List<Movie>) {
//        println("loaded on splash count = ${movies.count()}")
        repo.insertAll(movies) {
//            println("done saving first time")
            startActivity(Intent(this, MovieListActivity::class.java))
        }
    }
}
