package com.abloom.mery.presentation.ui.writeanswer

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentWriteAnswerBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import com.abloom.mery.presentation.common.util.repeatOnStarted
import com.abloom.mery.presentation.common.view.ConfirmDialog
import com.abloom.mery.presentation.common.view.setOnActionClick
import com.abloom.mery.presentation.common.view.setOnNavigationClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteAnswerFragment :
    BaseFragment<FragmentWriteAnswerBinding>(R.layout.fragment_write_answer) {

    private val writeAnswerViewModel: WriteAnswerViewModel by viewModels()
    private val args: WriteAnswerFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAppBar()
        setUpViewModel()
        setUpQuestion()
        observeAnswer()
    }

    private fun setUpAppBar() {
        binding.appbarWriteAnswer.setOnNavigationClick {
            showPopBackDialog()
        }

        binding.appbarWriteAnswer.setOnActionClick {
            showCompleteDialog()
        }
    }

    private fun setUpViewModel() {
        binding.viewModel = writeAnswerViewModel
    }

    private fun setUpQuestion() {
        repeatOnStarted {
            writeAnswerViewModel.question.collect {
                binding.question = it?.content ?: QUESTION_NULL
            }
        }
    }

    private fun observeAnswer() {
        repeatOnStarted {
            writeAnswerViewModel.answer.collect {
                binding.textSize = it.length.toString()
                when (it.length) {
                    0 -> {
                        binding.isEnabled = false
                    }

                    else -> {
                        if (it.length <= TEXT_LIMIT_SIZE) {
                            binding.isEnabled = true
                            binding.tvTextLength.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.neutral_40
                                )
                            )
                        } else {
                            binding.isEnabled = false
                            binding.tvTextLength.setTextColor(Color.parseColor(TEXT_RED_COLOR))
                        }
                    }
                }
            }
        }
    }

    private fun showPopBackDialog() {
        ConfirmDialog(
            context = requireContext(),
            title = getString(R.string.writeanswer_popback_confirm_dialog_title),
            message = getString(R.string.writeanswer_popback_confirm_dialog_message),
            positiveButtonLabel = getString(R.string.writeanswer_exit),
            onPositiveButtonClick = {
                findNavController().popBackStack(
                    R.id.createQnaFragment,
                    false
                )
            },
            negativeButtonLabel = getString(R.string.all_cancel),
        ).show()
    }

    private fun showCompleteDialog() {
        ConfirmDialog(
            context = requireContext(),
            title = getString(R.string.writeanswer_complete_confirm_dialog_title),
            message = getString(R.string.writeanswer_complete_confirm_dialog_message),
            positiveButtonLabel = getString(R.string.writeanswer_complete),
            onPositiveButtonClick = { findNavController().popBackStack(R.id.homeFragment, false) },
            negativeButtonLabel = getString(R.string.all_cancel),
        ).show()
    }

    companion object {

        const val TEXT_LIMIT_SIZE = 150
        const val TEXT_RED_COLOR = "#FF0000"
        const val QUESTION_NULL = "질문 값이 없습니다."
    }
}


