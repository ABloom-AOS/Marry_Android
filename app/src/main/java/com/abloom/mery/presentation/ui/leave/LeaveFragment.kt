package com.abloom.mery.presentation.ui.leave

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentLeaveBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import com.abloom.mery.presentation.common.view.ConfirmDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaveFragment : BaseFragment<FragmentLeaveBinding>(R.layout.fragment_leave) {

    private val viewModel: LeaveViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
    }

    private fun setupDataBinding() {
        binding.onLeaveButtonClick = ::showLeaveConfirmDialog
        binding.onBackButtonClick = { findNavController().popBackStack() }
    }

    private fun showLeaveConfirmDialog() {
        ConfirmDialog(
            context = requireContext(),
            title = getString(R.string.leave_leave_confirm_dialog_title),
            message = getString(R.string.leave_leave_confirm_dialog_message),
            positiveButtonLabel = getString(R.string.leave_leave_confirm_dialog_positive_button_label),
            onPositiveButtonClick = ::handleLeaveButtonClick
        ).show()
    }

    private fun handleLeaveButtonClick() {
        viewModel.leave()
        findNavController().popBackStack(R.id.homeFragment, false)
    }
}
