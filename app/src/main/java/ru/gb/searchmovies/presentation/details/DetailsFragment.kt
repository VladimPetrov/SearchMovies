package ru.gb.searchmovies.presentation.details

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import ru.gb.searchmovies.R
import ru.gb.searchmovies.data.dto.Movie
import ru.gb.searchmovies.data.states.AppState
import ru.gb.searchmovies.databinding.FragmentDetailBinding
import ru.gb.searchmovies.showSnackBar

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var menu: Menu
    private lateinit var movieBundle: Movie
    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this)[DetailsViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Movie>(BUNDLE_EXTRA)?.let { movie ->
            movieBundle = movie
        }
        viewModel.liveDate.observe(viewLifecycleOwner, { appState ->
            renderData(appState)
        })
        loadMovie()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.details_screen_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.details_menu_item_add -> {
                item.isVisible = false
                menu.findItem(R.id.details_menu_item_cancel).isVisible = true
                menu.findItem(R.id.details_menu_item_OK).isVisible = true
                true
            }
            R.id.details_menu_item_OK -> {
                item.isVisible = false
                menu.findItem(R.id.details_menu_item_cancel).isVisible = false
                menu.findItem(R.id.details_menu_item_add).isVisible = true
                true
            }
            R.id.details_menu_item_cancel -> {
                item.isVisible = false
                menu.findItem(R.id.details_menu_item_OK).isVisible = false
                menu.findItem(R.id.details_menu_item_add).isVisible = true
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadMovie() {
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        viewModel.getMovieFromRemoteSource(movieBundle.id)
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
                    getString(R.string.reload), {
                        viewModel.getMovieFromRemoteSource(movieBundle.id)
                    })
            }
        }
    }

    private fun displayMovie(movie: Movie) {
        viewModel.saveMovieToDb(movie)
        with(binding) {
            mainView.visibility = View.VISIBLE
            loadingLayout.visibility = View.GONE

            movieName.text = movie.name
            genreTextView.text = movie.showGenres()
            popularityTextView.text = movie.popularity
            timeTextView.text = movie.runtime
            titleMovieTextView.text = movie.overview
            if (movie.posterPath.isNotEmpty()) {
                context?.let {
                    Glide.with(it)
                        .load(movie.getPosterUrl())
                        .override(200, 300)
                        .into(binding.posterMovie)
                }
            }
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