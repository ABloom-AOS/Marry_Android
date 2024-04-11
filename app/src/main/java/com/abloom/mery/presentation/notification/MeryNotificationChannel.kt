package com.abloom.mery.presentation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.annotation.StringRes
import com.abloom.mery.R

enum class MeryNotificationChannel(
    val id: String,
    @StringRes private val channelName: Int,
    @StringRes private val description: Int,
    private val importance: Int
) {

    TODAY_QUESTION(
        id = "channel_today_question",
        channelName = R.string.notification_today_question_channel_name,
        description = R.string.notification_today_question_channel_description,
        importance = NotificationManager.IMPORTANCE_DEFAULT
    ),
    FIANCE(
        id = "channel_fiance",
        channelName = R.string.notification_fiance_channel_name,
        description = R.string.notification_fiance_channel_description,
        importance = NotificationManager.IMPORTANCE_HIGH
    );

    fun create(context: Context) {
        val channel = NotificationChannel(id, context.getString(channelName), importance)
        channel.description = context.getString(description)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {

        fun createAll(context: Context) {
            entries.forEach { channel -> channel.create(context) }
        }
    }
}
