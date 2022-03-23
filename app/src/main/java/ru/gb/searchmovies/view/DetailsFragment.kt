package ru.gb.searchmovies.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.gb.searchmovies.data.Movie
import ru.gb.searchmovies.databinding.FragmentDetailBinding

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            arguments?.getParcelable<Movie>(BUNDLE_EXTRA)?.let { movie ->
            with(binding) {
                movieName.text = movie.name
                genreTextView.text = movie.genre.toString()
                popularityTextView.text = movie.popularity
                timeTextView.text = movie.time
                titleMovieTextView.text = movie.title
            }
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