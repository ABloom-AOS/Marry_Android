package com.abloom.mery.presentation.ui.writeanswer

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentWriteAnswerBinding
import com.abloom.mery.presentation.common.base.NavigationFragment
import com.abloom.mery.presentation.common.view.ConfirmDialog
import com.abloom.mery.presentation.common.view.setOnActionClick
import com.abloom.mery.presentation.common.view.setOnNavigationClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteAnswerFragment :
    NavigationFragment<FragmentWriteAnswerBinding>(R.layout.fragment_write_answer) {

    private val writeAnswerViewModel: WriteAnswerViewModel by viewModels()

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (writeAnswerViewModel.answer.value.isNotBlank()) {
                showBackConfirmDialog()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWindowInsetsListener(view)
        setupOnBackPressed()
        setupAppBar()
        setupDataBinding()
    }

    private fun setupWindowInsetsListener(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            val navigatorBarHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            if (imeVisible) {
                binding.root.setPadding(0, 0, 0, imeHeight - navigatorBarHeight)
            } else {
                binding.root.setPadding(0, 0, 0, 0)
            }
            insets
        }
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            owner = viewLifecycleOwner,
            onBackPressedCallback = onBackPressedCallback
        )
    }

    private fun showBackConfirmDialog() {
        ConfirmDialog(
            context = requireContext(),
            title = getString(R.string.writeanswer_popback_confirm_dialog_title),
            message = getString(R.string.writeanswer_popback_confirm_dialog_message),
            positiveButtonLabel = getString(R.string.writeanswer_exit),
            onPositiveButtonClick = { findNavController().popBackStack() },
            negativeButtonLabel = getString(R.string.all_cancel),
        ).show()
    }

    private fun setupAppBar() {
        binding.appbarWriteAnswer.setOnNavigationClick {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.appbarWriteAnswer.setOnActionClick { showCompleteConfirmDialog() }
    }

    private fun showCompleteConfirmDialog() {
        ConfirmDialog(
            context = requireContext(),
            title = getString(R.string.writeanswer_complete_confirm_dialog_title),
            message = getString(R.string.writeanswer_complete_confirm_dialog_message),
            positiveButtonLabel = getString(R.string.writeanswer_complete),
            onPositiveButtonClick = ::handleWriteAnswerConfirm,
            negativeButtonLabel = getString(R.string.all_cancel),
        ).show()
    }

    private fun handleWriteAnswerConfirm() {
        writeAnswerViewModel.answerQna()
        val isNavigateToHomeSuccess =
            findNavController().popBackStack(R.id.homeFragment, false)
        if (!isNavigateToHomeSuccess) findNavController().popBackStack()
    }

    private fun setupDataBinding() {
        binding.viewModel = writeAnswerViewModel
    }
}


