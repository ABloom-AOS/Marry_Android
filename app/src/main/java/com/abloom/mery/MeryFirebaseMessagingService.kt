package com.abloom.mery

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MeryFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // 포그라운드일때만 실행 됨. 즉 포그라운드와 백그라운드일때 처리가 달라짐.
        // 백그라운드는 그냥 앱만 켜지고, 포그라운드는 해당 지점까지는 이동 But 스택은 쌓이지 않음.

        if (remoteMessage.data.isNotEmpty()) {

            Log.e("TAG", "token 메세지 받음 , 현재 빈 데이터")

            val args = Bundle().apply {
                putString("viewToOpen", remoteMessage.data["viewToOpen"])
                putString("qid", remoteMessage.data["qid"])
            }

            val qnaPendingIntent = NavDeepLinkBuilder(applicationContext)
                .setGraph(R.navigation.app)
                .setDestination(R.id.qnaFragment)
                .setArguments(args)
                .createPendingIntent()

            sendNotification(
                remoteMessage.notification?.title,
                remoteMessage.notification?.body,
                qnaPendingIntent
            )
        } else {

        }

        remoteMessage.notification?.let {
            val pending = NavDeepLinkBuilder(applicationContext)
                .setGraph(R.navigation.app)
                .setDestination(R.id.homeFragment)
                .createPendingIntent()

            sendNotification(
                it.title,
                it.body,
                pending
            )
        }
    }

    private fun sendNotification(title: String?, body: String?, pendingIntent: PendingIntent) {

        Log.e("cyc", "sendNotification")

        val notifyId = (System.currentTimeMillis() / 7).toInt()

        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(notifyId, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("IDService", "Refreshed token: $token")
        //         updateFcmToken(token)
        // 파이어베이스에 token 업로드 데이터를 직접 건드는 부분이기 때문에  주석처리 하였음.
    }

    private fun updateFcmToken(fcmToken: String) {
        val myId = Firebase.auth.currentUser?.uid
        if (myId != null) {
            val userRef = Firebase.firestore.collection("users").document(myId)
            userRef.update("fcmToken", fcmToken)
                .addOnSuccessListener { Log.e("TAG", "FCM Token for user $myId updated") }
                .addOnFailureListener { e ->
                    Log.e(
                        "TAG",
                        "Error updating FCM Token for user $myId",
                        e
                    )
                }
        }
    }

    companion object {

        private const val CHANNEL_ID = "channel_mery"
    }
}
