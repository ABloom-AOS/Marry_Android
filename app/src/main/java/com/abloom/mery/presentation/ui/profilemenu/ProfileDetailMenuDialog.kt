package com.abloom.mery.presentation.ui.profilemenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.abloom.mery.databinding.DialogProfileDetailMenuBinding
import com.abloom.mery.presentation.ui.profilemenu.marriagedatechange.MarriageDateChangeDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProfileDetailMenuDialog : BottomSheetDialogFragment() {

    private var _binding: DialogProfileDetailMenuBinding? = null
    private val binding get() = _binding!!

    private val bottomSheet: FrameLayout by lazy {
        val parentView = binding.root.parent as View
        parentView.findViewById(com.google.android.material.R.id.design_bottom_sheet)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogProfileDetailMenuBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheet.setupBackground()
        bottomSheet.setupBehavior()
        setupDataBinding()
    }

    private fun FrameLayout.setupBackground() {
        background = null
    }

    private fun FrameLayout.setupBehavior() {
        val behavior = BottomSheetBehavior.from(this)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }

    private fun setupDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.onNameChangeButtonClick = ::handleNameChangeButtonClick
        binding.onMarriageDateChangeButtonClick = ::handleMarriageDateChangeButtonClick
        binding.onCancelButtonClick = ::dismiss
    }

    private fun handleNameChangeButtonClick() {
        NameChangeDialog().show(parentFragmentManager, null)
        dismiss()
    }

    private fun handleMarriageDateChangeButtonClick() {
        MarriageDateChangeDialog().show(parentFragmentManager, null)
        dismiss()
    }
}
