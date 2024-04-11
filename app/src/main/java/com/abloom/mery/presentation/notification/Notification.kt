package com.abloom.mery.presentation.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.abloom.mery.R

private const val TODAY_QUESTION_NOTIFICATION_ID = 1

fun Context.notifyTodayQuestion(questionId: Long) {
    val args = Bundle().apply { putLong("question_id", questionId) }
    val intent = NavDeepLinkBuilder(this)
        .setGraph(R.navigation.app)
        .setDestination(R.id.writeAnswerFragment)
        .setArguments(args)
        .createPendingIntent()
    val notification = createNotification(
        channelId = MeryNotificationChannel.TODAY_QUESTION.id,
        title = getString(R.string.push_today_question_receive),
        text = getString(R.string.push_review_comment_search_each_mind),
        intent = intent
    )
    notify(notificationId = TODAY_QUESTION_NOTIFICATION_ID, notification = notification)
}

fun Context.notifyFianceAction(title: String, body: String, questionId: Long) {
    val args = Bundle().apply { putLong("question_id", questionId) }
    val intent = NavDeepLinkBuilder(applicationContext)
        .setGraph(R.navigation.app)
        .setDestination(R.id.qnaFragment)
        .setArguments(args)
        .createPendingIntent()
    val notification = createNotification(
        channelId = MeryNotificationChannel.FIANCE.id,
        title = title,
        text = body,
        intent = intent
    )
    val notificationId = System.currentTimeMillis().toInt()
    notify(notificationId = notificationId, notification = notification)
}

fun Context.notifyConnectSuccess(title: String, body: String) {
    val intent = NavDeepLinkBuilder(applicationContext)
        .setGraph(R.navigation.app)
        .setDestination(R.id.homeFragment)
        .createPendingIntent()
    val notification = createNotification(
        channelId = MeryNotificationChannel.FIANCE.id,
        title = title,
        text = body,
        intent = intent
    )
    val notificationId = System.currentTimeMillis().toInt()
    notify(notificationId = notificationId, notification = notification)
}

private fun Context.createNotification(
    channelId: String,
    title: String,
    text: String,
    intent: PendingIntent
) = NotificationCompat.Builder(this, channelId)
    .setSmallIcon(R.mipmap.mery_app_icon)
    .setContentTitle(title)
    .setContentText(text)
    .setAutoCancel(true)
    .setContentIntent(intent)
    .build()

private fun Context.notify(notificationId: Int, notification: Notification) {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(notificationId, notification)
}
