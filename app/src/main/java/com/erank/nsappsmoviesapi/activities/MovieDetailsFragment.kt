package com.erank.nsappsmoviesapi.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.erank.nsappsmoviesapi.R
import com.erank.nsappsmoviesapi.models.Movie
import com.erank.nsappsmoviesapi.utils.MovieRepository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_movie_details.*

class MovieDetailsFragment : Fragment() {

    companion object {
        const val movieIndex = "movieIndex"
        fun newInstance(index: Int) =
            MovieDetailsFragment().apply {
                arguments = Bundle().apply{ putInt(movieIndex, index) }
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movie_details, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val i = arguments!!.getInt(movieIndex)
        if (i != -1)
            fillMovie(MovieRepository.getInstance(view.context).movies[i])
        else
            fragmentManager?.popBackStack()
    }


    private fun fillMovie(movie: Movie) = with(movie) {
        Picasso.get().load(image)
            .fit()
            .placeholder(R.drawable.ic_movie)
            .into(movie_imgview)

        titleTV.text = title

        rating_txtview.text = getString(R.string.rating, rating, 10)

        release_txtview.text = getString(R.string.ryear, releaseYear)
        genre_txtview.text = getString(R.string.genre, genres.joinToString(", "))
    }


    //    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_movie_details)
//
//        val i = intent.getIntExtra(movieIndex, -1)
//        if (i != -1)
//            fillMovie(MovieRepository.getInstance(this).movies[i])
//        else
//            finish()
//    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        item?.let {
//            when (item.itemId) {
//                android.R.id.home -> {
//                    val slideInLeft = android.R.anim.slide_in_left
//                    val slideOutRight = android.R.anim.slide_out_right
//                    overridePendingTransition(slideInLeft, slideOutRight)
//                }
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
}