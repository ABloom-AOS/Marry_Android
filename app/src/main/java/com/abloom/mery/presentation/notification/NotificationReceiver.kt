package com.abloom.mery.presentation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.abloom.mery.R

class NotificationReceiver : BroadcastReceiver() {

    private lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        setupNotificationChannel(context)
        setupNotification(context)
    }

    private fun setupNotificationChannel(context: Context) {

        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.description = context.getString(R.string.app_name)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun setupNotification(context: Context) {

        val createQnaPendingIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.app)
            .setDestination(R.id.createQnaFragment)
            .createPendingIntent()

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.mery_app_icon)
            .setContentTitle(context.getString(R.string.push_today_question_receive))
            .setContentText(context.getString(R.string.push_review_comment_search_each_mind))
            .setAutoCancel(true)
            .setContentIntent(createQnaPendingIntent)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {

        private const val CHANNEL_ID = "channel_mery"
        private const val CHANNEL_NAME = "channel_name_mery"
        private const val NOTIFICATION_ID = 0
    }
}
