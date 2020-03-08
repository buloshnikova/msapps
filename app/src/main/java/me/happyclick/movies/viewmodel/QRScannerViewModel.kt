package me.happyclick.movies.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import me.happyclick.movies.model.Movie
import me.happyclick.movies.model.MoviesDatabase
import me.happyclick.movies.util.sortMoviesByYear

class QRScannerViewModel(application: Application) : BaseViewModel(application) {

    private val disposable = CompositeDisposable()

    val movieExists = MutableLiveData<Boolean>()
    val movieAdded = MutableLiveData<Boolean>()

    fun checkMovieInDB(movie: Movie) {

        launch {
            val dao = MoviesDatabase(getApplication()).movieDao()
            val result = dao.getMovieByTitle(movie.title!!)
            movieExists.value = result !== null
        }
    }

    fun addMovie(movie: Movie) {
        launch {
            val dao = MoviesDatabase(getApplication()).movieDao()
            val movies = dao.getAllMovies()
            movies.add(movie)
            val sortedMoviesList= sortMoviesByYear(movies)
            dao.deleteAllMovies()
            val result = dao.insertAll(*sortedMoviesList.toTypedArray())
            movieAdded.value = result.size == sortedMoviesList.size
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}