package me.happyclick.movies.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MoviesApiService {

    private val MOVIES_BASE_URL = "https://api.androidhive.info"

    private val api = Retrofit.Builder()
        .baseUrl(MOVIES_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build().create(MoviesApi::class.java)

    fun getMovies(): Single<List<Movie>> {
        return api.getMovies()
    }
}