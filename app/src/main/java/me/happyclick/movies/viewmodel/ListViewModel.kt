package me.happyclick.movies.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import me.happyclick.movies.model.Movie
import me.happyclick.movies.model.MoviesDatabase
import me.happyclick.movies.model.MoviesApiService
import me.happyclick.movies.util.NotificationsHelper
import me.happyclick.movies.util.SharedPreferencesHelper
import me.happyclick.movies.util.sortMoviesByYear

class ListViewModel(application: Application) : BaseViewModel(application) {

    private var prefHelper = SharedPreferencesHelper(getApplication())
    private val moviesService = MoviesApiService()
    private val disposable = CompositeDisposable()

    val movies = MutableLiveData<List<Movie>>()
    val moviesLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun refresh() {
        fetchFromDatabase()
    }

    fun refreshBypassCache() {
        fetchFromRemote()
    }

    private fun fetchFromDatabase() {
        loading.value = true

        launch {
            val movies = MoviesDatabase(getApplication()).movieDao().getAllMovies()
            if(movies.isNotEmpty()) {
                moviesRetrieved(movies)
            } else {
                fetchFromRemote()
            }
        }
    }

    private fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            moviesService.getMovies()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Movie>>() {
                    override fun onSuccess(movieList: List<Movie>) {
                        storeMoviesLocally(movieList)
                        Toast.makeText(getApplication(), "Retrieved from remote", Toast.LENGTH_LONG)
                            .show()
                        NotificationsHelper(getApplication()).createNotification()
                    }

                    override fun onError(e: Throwable) {
                        moviesLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun moviesRetrieved(movieList: List<Movie>) {
        movies.value = movieList
        moviesLoadError.value = false
        loading.value = false
    }

    private fun storeMoviesLocally(list: List<Movie>) {

        val sortedMoviesList= sortMoviesByYear(list)

        launch {
            val dao = MoviesDatabase(getApplication()).movieDao()
            dao.deleteAllMovies()
            val result = dao.insertAll(*sortedMoviesList.toTypedArray())
            var i = 0
            while (i < sortedMoviesList.size) {
                sortedMoviesList[i].uuid = result[i].toInt()
                ++i
            }
            moviesRetrieved(sortedMoviesList)
        }

        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}