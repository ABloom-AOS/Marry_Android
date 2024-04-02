package com.abloom.mery.presentation.ui.home.qnasrecyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.abloom.domain.qna.model.Qna

class QnaAdapter(
    private val onQnaClick: (questionId: Long) -> Unit,
) : ListAdapter<Qna, QnaViewHolder>(QnaDiffUtil()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QnaViewHolder = QnaViewHolder(parent = parent, onQnaClick = onQnaClick)

    override fun onBindViewHolder(holder: QnaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}




