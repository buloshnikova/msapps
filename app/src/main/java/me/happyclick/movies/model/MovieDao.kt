package me.happyclick.movies.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface MovieDao {
    @Insert
    suspend fun insertAll(vararg movies: Movie): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie): Long

    @Query("SELECT * FROM movie")
    suspend fun getAllMovies(): MutableList<Movie>

    @Query("SELECT * FROM movie WHERE uuid = :movieId")
    suspend fun getMovie(movieId: Int): Movie

    @Query("DELETE FROM movie")
    suspend fun deleteAllMovies()

    @Query("SELECT * FROM movie WHERE title = :title")
    suspend fun getMovieByTitle(title: String): Movie?

}