package com.abloom.mery.presentation.ui.qna

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.abloom.mery.R
import com.abloom.mery.databinding.DialogResponseSelectBinding

class ResponseSelectDialog : DialogFragment() {

    private val viewModel: QnaViewModel by viewModels(ownerProducer = { requireParentFragment() })

    private var _binding: DialogResponseSelectBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogResponseSelectBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupBackground()
    }

    private fun setupDataBinding() {
        binding.viewModel = viewModel
        binding.onCloseButtonClick = ::dismiss
        binding.onCompleteButtonClick = { response ->
            viewModel.respondToQna(response)
            dismiss()
        }
    }

    private fun setupBackground() {
        dialog?.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_all_dialog_big)
        )
    }

    companion object {

        const val TAG = "TAG_RESPONSE_SELECT_DIALOG"
    }
}
