package com.abloom.mery

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MeryFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {

            Log.e("TAG", "token 메세지 받음")

            val args = Bundle().apply {
                putInt("id", 4)
            }

            val pending = NavDeepLinkBuilder(applicationContext)
                .setGraph(R.navigation.app)
                .setDestination(R.id.homeFragment)
                .setArguments(args)
                .createPendingIntent()

            sendNotification(
                remoteMessage.notification?.title,
                remoteMessage.notification?.body,
                pending
            )
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("IDService", "Refreshed token: $token")

        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        // 디바이스 토큰이 생성되거나 재생성 될 시 동작할 코드 작성
    }

    private fun sendNotification(title: String?, body: String?, pendingIntent: PendingIntent) {

        val notifyId = (System.currentTimeMillis() / 7).toInt()

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notifyId, notificationBuilder.build())
    }

    companion object {
        private const val CHANNEL_ID = "channel_mery"
        private const val CHANNEL_NAME = "push"
    }


}