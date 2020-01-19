package me.happyclick.movies.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import me.happyclick.movies.R
import me.happyclick.movies.view.MainActivity

class NotificationsHelper(val context: Context) {

    private val CHANNEL_ID = "Movie channel id"
    private val NOTIFICATION_ID = 123

    fun createNotification() {
        createNotificationChannel()
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val icon = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_movie_icon)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.film)
            .setLargeIcon(icon)
            .setContentTitle("Movies retrieved")
            .setContentText("Test notification after retrieving the list of movies")
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(icon)
                    .bigLargeIcon(icon)
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }
    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_ID
            val descriptionText = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
                .apply {
                    description = descriptionText
                }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}