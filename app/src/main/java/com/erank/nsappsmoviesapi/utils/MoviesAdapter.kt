package com.erank.nsappsmoviesapi.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erank.nsappsmoviesapi.R
import com.erank.nsappsmoviesapi.models.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_cell.view.*


class MoviesAdapter(private var onMovieTappedListener: OnMovieTappedListener) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    //gets the movies straight from repository
    val movies: List<Movie> get() = MovieRepository.getInstance().movies

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val cellForRow = inflater.inflate(R.layout.movie_cell, parent, false)
        return MovieViewHolder(cellForRow)
    }

    override fun getItemCount() = movies.count()

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        with(movies[position]) {

            holder.itemView.movie_image_view.let {

            Picasso.get().load(image)
                .fit()
                .placeholder(R.drawable.empty_img)
                .transform(RoundedCornersTransform(16, 0))
                .into(it)
            }


            holder.itemView.movie_title.text = title
            holder.itemView.year_tv.text = releaseYear.toString()

        }
        holder.onMovieTappedListener = this.onMovieTappedListener
    }

    class MovieViewHolder(
        itemView: View,
        var onMovieTappedListener: OnMovieTappedListener? = null
    ) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onMovieTappedListener?.onMovieTapped(adapterPosition)
        }
    }

    interface OnMovieTappedListener {
        fun onMovieTapped(position: Int)
    }
}
