package com.abloom.mery.presentation.ui.home.qnasrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abloom.domain.qna.model.Qna
import com.abloom.mery.R
import com.abloom.mery.databinding.ItemHomeQnaBinding

class QnaViewHolder(
    parent: ViewGroup,
    onQnaClick: (questionId: Long) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_home_qna, parent, false)
) {

    private val binding = ItemHomeQnaBinding.bind(itemView)

    init {
        binding.onQnaClick = { onQnaClick(it) }
    }

    fun bind(qna: Qna) {
        binding.qna = qna
    }
}
