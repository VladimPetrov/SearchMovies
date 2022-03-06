package ru.gb.searchmovies.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.gb.searchmovies.R
import ru.gb.searchmovies.data.Movie

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
                findViewById<TextView>(R.id.itemMovieName).text = movie.name
                findViewById<TextView>(R.id.itemMovieGenre).text = movie.genre.toString()
                findViewById<TextView>(R.id.itemMoviePopularity).text = movie.popularity
                setOnClickListener {
                    onItemViewClickListener?.onItemClick(movie)
                }
            }
        }
    }
}