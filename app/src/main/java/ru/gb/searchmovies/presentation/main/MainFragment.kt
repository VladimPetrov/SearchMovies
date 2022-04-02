package ru.gb.searchmovies.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import ru.gb.searchmovies.R
import ru.gb.searchmovies.data.SharedPrefsConstants
import ru.gb.searchmovies.data.states.AppState
import ru.gb.searchmovies.data.dto.Movie
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
    private var isAdultMovie: Boolean = false
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        activity?.let {
            isAdultMovie = it.getPreferences(Context.MODE_PRIVATE).getBoolean(SharedPrefsConstants.IS_ADULT_KEY,true)
        }
        menu.findItem(R.id.option_menu_item_adult).isChecked = isAdultMovie
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.option_menu_item_adult) {
            item.isChecked = !item.isChecked
            val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
            val editor = sharedPrefs?.edit()
            editor?.let {
                it.putBoolean(SharedPrefsConstants.IS_ADULT_KEY,isAdultMovie)
                it.apply()
            }
            loadMovies()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainFragmentRecyclerView.adapter = adapter
        binding.mainFragmentFAB.setOnClickListener { changeMovieDataset() }
        viewModel.liveDate.observe(viewLifecycleOwner, { renderData(it) })
        loadMovies()
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
                    action = { loadMovies() }
                )
            }
        }
    }

    private fun loadMovies() {
        activity?.let {
            isDataSetMovie = it.getPreferences(Context.MODE_PRIVATE).getBoolean(SharedPrefsConstants.IS_MOVIES_KEY,true)
            isAdultMovie = it.getPreferences(Context.MODE_PRIVATE).getBoolean(SharedPrefsConstants.IS_ADULT_KEY,true)
        }
        viewModel.getMovieFromLocalSource(isDataSetMovie,isAdultMovie)
    }

    private fun changeMovieDataset() {
        isDataSetMovie = !isDataSetMovie
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPrefs?.edit()
        editor?.let {
            it.putBoolean(SharedPrefsConstants.IS_MOVIES_KEY,isDataSetMovie)
            it.apply()
        }
        loadMovies()
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