package ru.gb.searchmovies.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import ru.gb.searchmovies.data.Movie
import ru.gb.searchmovies.data.MovieDTO
import ru.gb.searchmovies.data.MovieLoader
import ru.gb.searchmovies.databinding.FragmentDetailBinding

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieBundle: Movie
    private val loadListener  = object : MovieLoader.MovieLoaderListener {
        override fun onLoaded(movieDTO: MovieDTO) {
            displayMovie(movieDTO)
        }

        override fun onFailed(throwable: Throwable) {
            TODO("Not yet implemented")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            arguments?.getParcelable<Movie>(BUNDLE_EXTRA)?.let { movie ->
                           movieBundle = movie
            }
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        loadMovie()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadMovie() {
        val movieLoader = MovieLoader(loadListener, movieBundle.id)
        movieLoader.loadMovie()
    }

    private fun displayMovie(movieDTO: MovieDTO) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE

            movieName.text = movieDTO.name
            genreTextView.text = movieDTO.showGenres()
            popularityTextView.text = movieDTO.popularity
            timeTextView.text = movieDTO.runtime
            titleMovieTextView.text = movieDTO.overview
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        const val BUNDLE_EXTRA = "weather"

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}