package com.abloom.mery.presentation.ui.profilemenu

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.abloom.mery.databinding.DialogNameChangeBinding
import com.abloom.mery.presentation.common.util.dp

class NameChangeDialog : DialogFragment() {

    private val viewModel: ProfileMenuViewModel by viewModels(ownerProducer = { requireParentFragment() })

    private var _binding: DialogNameChangeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogNameChangeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupWindow()
    }

    private fun setupWindow() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes?.let {
            it.width = 270.dp
            it.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
    }

    private fun setupDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.onPositiveButtonClick = ::handlePositiveButtonClick
        binding.onNegativeButtonClick = ::dismiss
    }

    private fun handlePositiveButtonClick() {
        viewModel.changeName(binding.etNamechangedialogNameInput.text.toString())
        dismiss()
    }
}
