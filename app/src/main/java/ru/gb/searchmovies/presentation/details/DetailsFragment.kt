package ru.gb.searchmovies.presentation.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.gb.searchmovies.R
import ru.gb.searchmovies.data.dto.Movie
import ru.gb.searchmovies.data.states.AppState
import ru.gb.searchmovies.databinding.FragmentDetailBinding
import ru.gb.searchmovies.showSnackBar

private const val MAIN_LINK = "https://api.themoviedb.org/3/movie/"

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieBundle: Movie
    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this)[DetailsViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            arguments?.getParcelable<Movie>(BUNDLE_EXTRA)?.let { movie ->
                           movieBundle = movie
            }
        viewModel.liveDate.observe(viewLifecycleOwner,{ appState ->
            renderData(appState)
        })
        loadMovie()
    }

    private fun loadMovie() {
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        viewModel.getMovieFromRemoteSource(MAIN_LINK+"${movieBundle.id}")
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessDetails -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE
                displayMovie(appState.movie)
            }
            is AppState.Loading -> {
                binding.mainView.visibility = View.GONE
                binding.loadingLayout.visibility = View.VISIBLE
            }
           else -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE
                binding.mainView.showSnackBar(
                    getString(R.string.error),
                getString(R.string.reload),{
                    viewModel.getMovieFromRemoteSource(MAIN_LINK+"${movieBundle.id}")
                    })
            }
        }
    }

    private fun displayMovie(movie: Movie) {
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE

            movieName.text = movie.name
            genreTextView.text = movie.showGenres()
            popularityTextView.text = movie.popularity
            timeTextView.text = movie.runtime
            titleMovieTextView.text = movie.overview
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val BUNDLE_EXTRA = "movies"

    }

}