package me.happyclick.movies.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_movie.view.*
import me.happyclick.movies.R
import me.happyclick.movies.databinding.ItemMovieBinding
import me.happyclick.movies.model.Movie

class MoviesListAdapter(val moviesList: ArrayList<Movie>): RecyclerView.Adapter<MoviesListAdapter.MovieViewHolder>(), MovieClickListener {

    fun updateMovieList(newMoviesList: List<Movie>) {
        moviesList.clear()
        moviesList.addAll(newMoviesList)
        notifyDataSetChanged()
    }

    class MovieViewHolder(var view: ItemMovieBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        //val view = inflater.inflate(R.layout.item_movie, parent, false)
        val view = DataBindingUtil.inflate<ItemMovieBinding>(inflater, R.layout.item_movie, parent, false)

        return MovieViewHolder(view)
    }

    override fun getItemCount() = moviesList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.view.movie = moviesList[position]
        holder.view.listener = this
    }

    override fun onMovieClicked(v: View) {
        val action = ListFragmentDirections.actionGotoDetailFragment()
            action.movieUuid = v.movie_id.text.toString().toInt()
            Navigation.findNavController(v).navigate(action)
    }
}