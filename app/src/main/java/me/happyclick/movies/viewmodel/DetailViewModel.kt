package me.happyclick.movies.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import me.happyclick.movies.model.Movie
import me.happyclick.movies.model.MoviesDatabase

class DetailViewModel(application: Application) : BaseViewModel(application) {

    private val disposable = CompositeDisposable()

    val movieFull = MutableLiveData<Movie>()

    fun fetch(movieUuid: Int) {

        launch {
            val movie = MoviesDatabase(getApplication()).movieDao().getMovie(movieUuid)
            movieFull.value = movie
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


}