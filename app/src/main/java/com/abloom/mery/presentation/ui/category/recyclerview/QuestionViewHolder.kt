package com.abloom.mery.presentation.ui.category.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abloom.domain.question.model.Question
import com.abloom.mery.databinding.ItemCategoryBinding

class QuestionViewHolder(
    private val binding: ItemCategoryBinding,
    onQuestionClick: (questionId: Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.onQuestionClick = onQuestionClick
    }

    fun bind(question: Question) {
        binding.question = question
    }

    companion object {

        fun from(
            parent: ViewGroup,
            onQuestionClick: (questionId: Long) -> Unit
        ): QuestionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)
            return QuestionViewHolder(binding, onQuestionClick)
        }
    }
}
