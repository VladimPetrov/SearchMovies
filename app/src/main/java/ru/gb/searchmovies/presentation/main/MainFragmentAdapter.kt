package ru.gb.searchmovies.presentation.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.gb.searchmovies.R
import ru.gb.searchmovies.data.dto.Movie
import ru.gb.searchmovies.data.dto.URL_POSTER

class MainFragmentAdapter(
    private var onItemViewClickListener: MainFragment.onOnItemViewClickListener?
) : RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {

    private var movieData: List<Movie> = listOf()

    fun setData(data: List<Movie>) {
        movieData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(movieData[position])
    }

    override fun getItemCount() = movieData.size

    fun removeListener() {
        onItemViewClickListener = null
    }

    inner class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(movie: Movie) {
            itemView.apply {
                if (movie.posterPath.isNotEmpty()) {
                    context?.let {
                        Glide.with(it)
                            .load(URL_POSTER + movie.posterPath)
                            .override(80, 120)
                            .into(findViewById(R.id.posterMovie))
                    }
                }
                findViewById<TextView>(R.id.itemMovieName).text = movie.name
                findViewById<TextView>(R.id.itemMovieGenre).text = movie.showGenres()
                findViewById<TextView>(R.id.itemMoviePopularity).text = movie.popularity
                setOnClickListener {
                    onItemViewClickListener?.onItemClick(movie)
                }
            }
        }
    }
}