package com.abloom.mery.presentation.ui.home.qnasrecyclerview

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.abloom.domain.qna.model.FinishedQna
import com.abloom.domain.qna.model.Qna
import com.abloom.domain.qna.model.ResponseResult
import com.abloom.domain.qna.model.UnconnectedQna
import com.abloom.domain.qna.model.UnfinishedAnswerQna
import com.abloom.domain.qna.model.UnfinishedResponseQna
import com.abloom.domain.question.model.Category
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

        updateCategoryTag(qna.question.category)
        updateInfoTag(qna)
    }

    private fun updateCategoryTag(category: Category) {
        binding.tvHomeqnaCategoryTag.text = category.getDisplayName()
    }

    private fun Category.getDisplayName(): String {
        val stringRes = when (this) {
            Category.FINANCE -> R.string.category_finance
            Category.COMMUNICATION -> R.string.category_communication
            Category.VALUES -> R.string.category_values
            Category.LIFESTYLE -> R.string.category_lifestyle
            Category.CHILD -> R.string.category_children
            Category.FAMILY -> R.string.category_family
            Category.SEX -> R.string.category_sex
            Category.HEALTH -> R.string.category_health
            Category.WEDDING -> R.string.category_wedding
            Category.FUTURE -> R.string.category_future
            Category.PRESENT -> R.string.category_present
            Category.PAST -> R.string.category_past
        }
        return itemView.context.getString(stringRes)
    }

    private fun updateInfoTag(qna: Qna) {
        binding.tvHomeqnaInfoTag.setTextColor(qna.getTextColor())
        binding.tvHomeqnaInfoTag.text = qna.getInfoString()
        binding.tvHomeqnaInfoTag.background = qna.getBackground()
    }

    private fun Qna.getTextColor(): Int {
        val colorRes = when (this) {
            is UnconnectedQna -> R.color.neutral_60
            is UnfinishedAnswerQna -> if (loginUserAnswer == null) R.color.white else R.color.neutral_60
            is UnfinishedResponseQna -> if (loginUserResponse == null) R.color.white else R.color.neutral_60
            is FinishedQna -> when (responseResult) {
                ResponseResult.DOING_WELL -> R.color.white
                ResponseResult.MORE_TALK, ResponseResult.MORE_FIND -> R.color.neutral_60
            }
        }
        return ContextCompat.getColor(itemView.context, colorRes)
    }

    private fun Qna.getInfoString(): String {
        val stringRes = when (this) {
            is UnconnectedQna -> R.string.home_qna_info_tag_waiting_answer
            is UnfinishedAnswerQna -> if (loginUserAnswer == null) R.string.home_qna_info_tag_need_answer else R.string.home_qna_info_tag_waiting_answer
            is UnfinishedResponseQna -> if (loginUserResponse == null) R.string.home_qna_info_tag_need_response else R.string.home_qna_info_tag_waiting_response
            is FinishedQna -> when (responseResult) {
                ResponseResult.DOING_WELL -> R.string.home_qna_info_tag_complete
                ResponseResult.MORE_TALK -> R.string.qna_more_talk_response_result_label
                ResponseResult.MORE_FIND -> R.string.qna_more_find_response_result_label
            }
        }
        return itemView.context.getString(stringRes)
    }

    private fun Qna.getBackground(): Drawable? {
        val drawableRes = when (this) {
            is UnconnectedQna -> R.drawable.bg_homeqna_status_tag
            is UnfinishedAnswerQna -> if (loginUserAnswer == null) R.drawable.bg_homeqna_request_tag else R.drawable.bg_homeqna_status_tag
            is UnfinishedResponseQna -> if (loginUserResponse == null) R.drawable.bg_homeqna_request_tag else R.drawable.bg_homeqna_status_tag
            is FinishedQna -> when (responseResult) {
                ResponseResult.DOING_WELL -> R.drawable.bg_homeqna_complete_tag
                ResponseResult.MORE_TALK, ResponseResult.MORE_FIND -> R.drawable.bg_homeqna_status_tag
            }
        }
        return ContextCompat.getDrawable(itemView.context, drawableRes)
    }
}
