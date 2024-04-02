package com.abloom.mery.presentation.common.bindingadapter

import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abloom.mery.presentation.common.util.dp

@BindingAdapter("app:itemSpacing_dp")
fun RecyclerView.setItemSpacing(spacing: Int) {
    val layoutManager = this.layoutManager
    require(layoutManager is LinearLayoutManager) { "리사이클러 뷰가 LinearLayoutManager일 때만 해당 바인딩 어댑터를 사용할 수 있습니다." }

    val decoration = if (layoutManager.orientation == LinearLayout.HORIZONTAL) {
        RecyclerViewDecoration(width = spacing.dp)
    } else {
        RecyclerViewDecoration(height = spacing.dp)
    }

    addItemDecoration(decoration)
}

class RecyclerViewDecoration(
    private val width: Int = 0,
    private val height: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (position > 0) outRect.right = width
        if (position > 0) outRect.top = height
    }
}
