package com.abloom.mery.presentation.common.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import com.abloom.mery.R
import com.abloom.mery.databinding.DialogConfirmBinding
import com.abloom.mery.presentation.common.util.dp

class ConfirmDialog(
    context: Context,
    private val title: String,
    private val message: String? = null,
    private val positiveButtonLabel: String = context.getString(R.string.all_ok),
    private val negativeButtonLabel: String = context.getString(R.string.all_cancel),
    private val onPositiveButtonClick: () -> Unit = {},
    private val onNegativeButtonClick: () -> Unit = {},
    private val cancelable: Boolean = true,
    private val onCancel: () -> Unit = {},
) : Dialog(context) {

    private val binding: DialogConfirmBinding by lazy { DialogConfirmBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupDialogWindow()
        setupCancel()
        setupDataBinding()
    }

    private fun setupDialogWindow() {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.attributes?.let {
            it.width = 270.dp
            it.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }

    private fun setupCancel() {
        setCanceledOnTouchOutside(cancelable)
        setOnCancelListener { onCancel() }
    }

    private fun setupDataBinding() {
        binding.title = title
        binding.message = message
        binding.positiveButtonLabel = positiveButtonLabel
        binding.negativeButtonLabel = negativeButtonLabel
        binding.onPositiveButtonClick = {
            onPositiveButtonClick()
            dismiss()
        }
        binding.onNegativeButtonClick = {
            onNegativeButtonClick()
            dismiss()
        }
    }
}
