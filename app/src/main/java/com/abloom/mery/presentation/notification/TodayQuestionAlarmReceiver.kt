package com.abloom.mery.presentation.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.abloom.domain.question.usecase.GetTodayRecommendationQuestionUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TodayQuestionAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var getTodayRecommendationQuestionUseCase: GetTodayRecommendationQuestionUseCase

    override fun onReceive(context: Context, intent: Intent) {
        MainScope().launch {
            getTodayRecommendationQuestionUseCase().filterNotNull()
                .collect { question ->
                    context.notifyTodayQuestion(questionId = question.id)
                    cancel()
                }
        }
    }
}
