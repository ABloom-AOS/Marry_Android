package com.abloom.mery.presentation.ui.home.qnasrecyclerview

import androidx.recyclerview.widget.DiffUtil
import com.abloom.domain.qna.model.Qna

class QnaDiffUtil : DiffUtil.ItemCallback<Qna>() {

    override fun areItemsTheSame(oldItem: Qna, newItem: Qna): Boolean =
        oldItem.question.id == newItem.question.id

    override fun areContentsTheSame(oldItem: Qna, newItem: Qna): Boolean =
        oldItem == newItem
}
