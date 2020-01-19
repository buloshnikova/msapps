package me.happyclick.movies.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity //in case of a different from a class table name(tableName = "table_name")
data class Movie (

    @SerializedName("vote_count")
    val voteCount: Int?,

    @ColumnInfo(name = "movie_id")
    @SerializedName("id")
    val id: Int?,

    @SerializedName("video")
    val video: Boolean?,

    @SerializedName("vote_average")
    val voteAverage: Double?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("popularity")
    val popularity: Double?,

        @SerializedName("poster_path")
    val posterPath: String?,

    @SerializedName("original_language")
    val originalLanguage: String?,

    @SerializedName("original_title")
    val originalTitle: String?,

    @SerializedName("backdrop_path")
    val backdropPath: String?,

    @SerializedName("adult")
    val adult: Boolean?,

    @SerializedName("overview")
    val overview: String?,

    @SerializedName("release_date")
    val releaseDate: String? = null

) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}

@Entity
data class TopMovies (
    @SerializedName("page")
    val page: Int?,

    @SerializedName("total_results")
    val totalResults: Int?,

    @SerializedName("total_pages")
    val totalPages: Int?,

    @SerializedName("results")
    val movies: List<Movie>
)