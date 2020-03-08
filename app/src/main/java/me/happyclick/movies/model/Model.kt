package me.happyclick.movies.model

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken


@Entity //in case of a different from a class table name(tableName = "table_name")
data class Movie(

    @SerializedName("title")
    val title: String?,

    @SerializedName("rating")
    val popularity: Double?,

    @SerializedName("image")
    val posterPath: String?,

    @SerializedName("releaseYear")
    val releaseYear: Int?,

    @SerializedName("genre")
    @TypeConverters(StringListToGsonConverter::class)
    val genre: ArrayList<String>?

) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}

class StringListToGsonConverter {

    @TypeConverter
    fun restoreList(listOfString: String?): ArrayList<String?>? {
        return Gson().fromJson<ArrayList<String?>>(
            listOfString,
            object : TypeToken<ArrayList<String?>?>() {}.type
        )
    }

    @TypeConverter
    fun saveList(listOfString: ArrayList<String?>?): String? {
        return Gson().toJson(listOfString)
    }
}