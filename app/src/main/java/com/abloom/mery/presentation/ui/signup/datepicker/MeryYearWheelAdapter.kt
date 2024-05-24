package com.abloom.mery.presentation.ui.signup.datepicker

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.abloom.mery.R
import sh.tyy.wheelpicker.core.ItemEnableWheelAdapter
import sh.tyy.wheelpicker.core.TextWheelPickerView
import sh.tyy.wheelpicker.core.TextWheelViewHolder
import java.lang.ref.WeakReference

class MeryYearWheelAdapter(
    valueEnabledProvider: WeakReference<ValueEnabledProvider>
) :
    ItemEnableWheelAdapter(valueEnabledProvider) {

    override fun getItemCount() = Int.MAX_VALUE

    override val valueCount = Int.MAX_VALUE

    override fun onBindViewHolder(holder: TextWheelViewHolder, position: Int) {
        val text = SpannableString("$position")
        val isEnabled = valueEnabledProvider.get()?.isEnabled(this, position) ?: true
        holder.onBindData(
            TextWheelPickerView.Item(
                id = "$position",
                text = "$text" + "년",
                isEnabled = isEnabled
            )
        )
    }

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextWheelViewHolder {
        return TextWheelViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.mery_wheel_picker_item, parent, false) as TextView)
    }
}
