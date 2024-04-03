package com.abloom.mery

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("IDService", "Refreshed token: $token")

        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        // 디바이스 토큰이 생성되거나 재생성 될 시 동작할 코드 작성
    }
}
