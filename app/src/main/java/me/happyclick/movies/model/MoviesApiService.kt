package me.happyclick.movies.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MoviesApiService {

    private val MOVIEDB_API_KEY = "14854411ab91f4abc9bf5da3c08da9b2"
    private val MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie/"

    private val api = Retrofit.Builder()
        .baseUrl(MOVIEDB_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build().create(MoviesApi::class.java)

    fun getMovies() : Single<TopMovies> {
        return api.getUpcomingMovies(MOVIEDB_API_KEY)
    }
}