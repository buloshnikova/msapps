package me.happyclick.movies.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import me.happyclick.movies.R

private val MOVIEDB_BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w92"
private val MOVIEDB_BASE_BIG_IMAGE_URL = "https://image.tmdb.org/t/p/w342"

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_movie_icon)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}

@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    view.loadImage(MOVIEDB_BASE_IMAGE_URL + url, getProgressDrawable(view.context))
}

@BindingAdapter("android:bigImageUrl")
fun loadBigImage(view: ImageView, url: String?) {
    view.loadImage(MOVIEDB_BASE_BIG_IMAGE_URL + url, getProgressDrawable(view.context))
}