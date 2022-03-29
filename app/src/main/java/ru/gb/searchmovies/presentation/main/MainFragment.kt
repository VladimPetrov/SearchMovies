package ru.gb.searchmovies.presentation.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import ru.gb.searchmovies.R
import ru.gb.searchmovies.data.states.AppState
import ru.gb.searchmovies.data.dto.Movie
import ru.gb.searchmovies.data.dto.MovieDTO
import ru.gb.searchmovies.databinding.FragmentMainBinding
import ru.gb.searchmovies.hide
import ru.gb.searchmovies.presentation.details.DetailsFragment
import ru.gb.searchmovies.show
import ru.gb.searchmovies.showSnackBar

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private var isDataSetMovie: Boolean = true
    private val adapter = MainFragmentAdapter(object : onOnItemViewClickListener {
        override fun onItemClick(movie: Movie) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, DetailsFragment().also { fragment ->
                    fragment.arguments =
                        Bundle().also { bundle ->
                            bundle.putParcelable(
                                DetailsFragment.BUNDLE_EXTRA, movie
                            )
                        }
                })
                ?.addToBackStack("")?.commit()
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { changeMovieDataset() }
        viewModel.liveDate.observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getMovieFromLocalSource(isDataSetMovie)
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.hide()
                adapter.setData(appState.movie)
            }
            is AppState.Loading -> {
                binding.mainFragmentLoadingLayout.show()
            }
            else -> {
                binding.mainFragmentLoadingLayout.hide()
                binding.mainFragmentFAB.showSnackBar(
                    text = R.string.error,
                    actionText = R.string.reload,
                    action = { viewModel.getMovieFromLocalSource(isDataSetMovie) }
                )
            }
        }
    }

    private fun changeMovieDataset() {
        isDataSetMovie = !isDataSetMovie
        viewModel.getMovieFromLocalSource(isDataSetMovie)
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    interface onOnItemViewClickListener {
        fun onItemClick(movie: Movie)
    }
}