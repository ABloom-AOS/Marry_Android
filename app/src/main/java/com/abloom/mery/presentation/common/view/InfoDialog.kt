package com.abloom.mery.presentation.common.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import com.abloom.mery.R
import com.abloom.mery.databinding.DialogInfoBinding
import com.abloom.mery.presentation.common.extension.dp

class InfoDialog(
    context: Context,
    private val title: String,
    private val message: String? = null,
    private val buttonLabel: String = context.getString(R.string.all_ok),
    private val onButtonClick: (() -> Unit)? = null,
    private val cancelable: Boolean = true,
) : Dialog(context) {

    private val binding: DialogInfoBinding by lazy { DialogInfoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setCancelable(cancelable)
        setupDialogWindow()
        setupDataBinding()
    }

    private fun setupDialogWindow() {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.attributes?.let {
            it.width = 270.dp
            it.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }

    private fun setupDataBinding() {
        binding.title = title
        binding.message = message
        binding.buttonLabel = buttonLabel
        binding.onButtonClick = {
            onButtonClick?.invoke()
            dismiss()
        }
    }
}
