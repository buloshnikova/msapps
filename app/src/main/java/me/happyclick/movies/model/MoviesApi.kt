package me.happyclick.movies.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    @GET("json/movies.json")
    fun getMovies(): Single<List<Movie>>
}