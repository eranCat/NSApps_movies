package com.erank.nsappsmoviesapi.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import com.erank.nsappsmoviesapi.R
import com.erank.nsappsmoviesapi.utils.*
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MovieListActivity : AppCompatActivity(),
    MoviesAdapter.OnMovieTappedListener {

    private val repo by lazy { MovieRepository.getInstance(applicationContext) }

    var isDetailsOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        qrFab.setOnClickListener {
            IntentIntegrator(this)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                .setPrompt(getString(R.string.barcodeMsg))
//                .setCameraId(0)  // Use a specific camera of the device
                .setBeepEnabled(false)
                .setBarcodeImageEnabled(true)
                .initiateScan()
        }

        movies_recycler.adapter = MoviesAdapter(this)

        val spanCount = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> 3
            Configuration.ORIENTATION_LANDSCAPE -> 5
            else -> 3
        }
        (movies_recycler.layoutManager as? GridLayoutManager)?.spanCount = spanCount

        movies_recycler.setHasFixedSize(false)

        progressBar.visibility = View.GONE
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        isDetailsOpen = savedInstanceState.getBoolean("isDetailsOpen")
        if (isDetailsOpen) qrFab.hide() else qrFab.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("isDetailsOpen",isDetailsOpen)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        qrFab.show()
        isDetailsOpen = false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onMovieTapped(position: Int) {

        supportFragmentManager
            .beginTransaction()
            .addToBackStack("details")
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.frameForDetails, MovieDetailsFragment.newInstance(position))
            .commit()

        qrFab.hide()
        isDetailsOpen = true

//        val movieIntent = Intent(this, MovieDetailsFragment::class.java)
//        movieIntent.putExtra(MovieDetailsFragment.movieIndex, position)
//        startActivity(movieIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result == null) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }

        val contents = result.contents
        if (contents == null) {
            println("no contents scanned")
            return
        }

        toast(getString(R.string.scanned) + contents)
        //            try converting the contents into a movie
        progressBar.visibility = View.VISIBLE
        val movie = JSONApiHelper.convertMovieFromJson(contents)
        if (movie == null) {
            progressBar.visibility = View.GONE
            toast(getString(R.string.invalidQR))
            return
        }

        repo.addIfNotExist(movie) { wasAdded ->

            runOnUiThread {
                progressBar.visibility = View.GONE
                if (!wasAdded) {
                    snackbar(
                        rootLayout,
                        getString(R.string.movieExistsMsg),
                        Snackbar.LENGTH_LONG
                    )
                    return@runOnUiThread
                }

                val lastPosition = repo.movies.count() - 1
                movies_recycler.adapter?.notifyItemInserted(lastPosition)
                movies_recycler.smoothScrollToPosition(lastPosition)
            }
        }
    }
}
