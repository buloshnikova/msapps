package me.happyclick.movies.view


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_list.*

import me.happyclick.movies.R
import me.happyclick.movies.databinding.FragmentListBinding
import me.happyclick.movies.viewmodel.ListViewModel


class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel
    private lateinit var dataBinding: FragmentListBinding
    private val moviesListAdapter = MoviesListAdapter(arrayListOf())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        movies_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = moviesListAdapter

        }

        refresh_layout.setOnRefreshListener {
            movies_list.visibility = View.GONE
            list_error.visibility = View.GONE
            loading_view.visibility = View.VISIBLE
            viewModel.refreshBypassCache()
            refresh_layout.isRefreshing = false
        }

        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.movies.observe(this, Observer { movies ->
            movies?.let {
                movies_list.visibility = View.VISIBLE
                list_error.visibility = View.GONE
                moviesListAdapter.updateMovieList(movies)
            }
        })

        viewModel.moviesLoadError.observe( this, Observer { isError ->
            isError?.let {
                list_error.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer { isLoading->
            isLoading?.let {
                loading_view.visibility = if(it) View.VISIBLE else View.GONE
                if(it) {
                    list_error.visibility = View.GONE
                    movies_list.visibility = View.GONE
                }
            }
        })
    }

}