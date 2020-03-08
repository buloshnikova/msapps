package me.happyclick.movies.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_detail.*

import me.happyclick.movies.R
import me.happyclick.movies.databinding.FragmentDetailBinding
import me.happyclick.movies.model.Movie
import me.happyclick.movies.util.YOUTUBE_LINK_SEARCH_QUERY
import me.happyclick.movies.util.YOUTUBE_LINK_SEARCH_TRAILER
import me.happyclick.movies.viewmodel.DetailViewModel

class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel

    private var movieUuid = 0

    private lateinit var dataBinding: FragmentDetailBinding

    private var currentMovie: Movie? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(false)
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            movieUuid = DetailFragmentArgs.fromBundle(it).movieUuid
        }

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)

        viewModel.fetch(movieUuid)

        observeViewModel()

        movie_trailer_link.setOnClickListener {
            openYoutube()
        }
        movie_trailer_icon.setOnClickListener {
            openYoutube()
        }
    }

    fun observeViewModel() {
        viewModel.movieFull.observe(viewLifecycleOwner, Observer { movie ->
            currentMovie = movie
            movie?.let {
                dataBinding.movie = it
                dataBinding.genres = it.genre
            }
        })
    }

    private fun openYoutube() {
        val appIntent: Intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(YOUTUBE_LINK_SEARCH_QUERY.plus(currentMovie?.title).plus(
                YOUTUBE_LINK_SEARCH_TRAILER))
        )
        context?.startActivity(appIntent)
    }
}
