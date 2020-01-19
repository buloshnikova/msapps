package me.happyclick.movies.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import me.happyclick.movies.R
import me.happyclick.movies.databinding.FragmentDetailBinding
import me.happyclick.movies.model.Movie
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
        setHasOptionsMenu(true)
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

    }

    fun observeViewModel() {
        viewModel.movieFull.observe(this, Observer { movie ->
            currentMovie = movie
            movie?.let {
                dataBinding.movie = it
            }
        })
    }
}
