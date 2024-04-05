package com.abloom.mery

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {

    @SuppressLint("ServiceCast")
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, CHANNEL_NAME)
            .setContentTitle(context.getString(R.string.push_today_question_receive))
            .setContentText(context.getString(R.string.push_review_comment_search_each_mind))
            .setSmallIcon(R.drawable.ic_notification)
            .build()

        notificationManager.notify(CHANNEL_ID, notification)
    }
    // 알림 생성 및 표시 로직

    companion object {

        private const val CHANNEL_NAME = "channel_mery"
        private const val CHANNEL_ID = 1001
    }
}
