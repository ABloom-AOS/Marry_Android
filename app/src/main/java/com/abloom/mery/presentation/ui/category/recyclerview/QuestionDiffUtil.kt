package com.abloom.mery.presentation.ui.category.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.abloom.domain.question.model.Question

class QuestionDiffUtil : DiffUtil.ItemCallback<Question>() {

    override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean =
        oldItem == newItem
}
