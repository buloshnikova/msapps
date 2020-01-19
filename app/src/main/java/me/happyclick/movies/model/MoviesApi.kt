package me.happyclick.movies.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {

    @GET("upcoming")
    fun getUpcomingMovies(@Query("api_key") api_key: String): Single<TopMovies>
}