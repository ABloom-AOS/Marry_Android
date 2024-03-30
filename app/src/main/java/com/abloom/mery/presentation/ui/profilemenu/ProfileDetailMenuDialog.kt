package com.abloom.mery.presentation.ui.profilemenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import com.abloom.mery.databinding.DialogProfileDetailMenuBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProfileDetailMenuDialog : BottomSheetDialogFragment() {

    private var _binding: DialogProfileDetailMenuBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileMenuViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogProfileDetailMenuBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDialogBackground()
        setupDataBinding()
    }

    private fun setupDialogBackground() {
        val parentView = binding.root.parent as View
        val dialogView = parentView
            .findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        dialogView.background = null
    }

    private fun setupDataBinding() {
        binding.onCancelButtonClick = ::dismiss
    }
}
