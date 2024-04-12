package com.abloom.mery

import android.annotation.SuppressLint
import com.abloom.mery.presentation.notification.notifyConnectSuccess
import com.abloom.mery.presentation.notification.notifyFianceAction
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MeryFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title ?: return
        val body = remoteMessage.notification?.body ?: return

        if (remoteMessage.data.isEmpty()) {
            applicationContext.notifyConnectSuccess(title = title, body = body)
            return
        }
        applicationContext.notifyFianceAction(
            title = title,
            body = body,
            questionId = remoteMessage.data[KEY_QUESTION_ID]?.toLong() ?: return
        )
    }

    companion object {

        private const val KEY_QUESTION_ID = "qid"
    }
}
