package com.abloom.mery.presentation.ui.category.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.abloom.domain.question.model.Question

class QuestionAdapter(
    private val onQuestionClick: (questionId: Long) -> Unit
) : ListAdapter<Question, QuestionViewHolder>(QuestionDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder.from(parent, onQuestionClick)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
