package com.abloom.mery.presentation.ui.qna

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abloom.domain.qna.model.FinishedQna
import com.abloom.domain.qna.model.Qna
import com.abloom.domain.qna.model.Response
import com.abloom.domain.qna.model.ResponseResult
import com.abloom.domain.qna.model.UnconnectedQna
import com.abloom.domain.qna.model.UnfinishedAnswerQna
import com.abloom.domain.qna.model.UnfinishedResponseQna
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentQnaBinding
import com.abloom.mery.presentation.common.base.NavigationFragment
import com.abloom.mery.presentation.common.extension.repeatOnStarted
import com.abloom.mery.presentation.common.view.setOnNavigationClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class QnaFragment : NavigationFragment<FragmentQnaBinding>(R.layout.fragment_qna) {

    private val viewModel: QnaViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAppBar()
        setupDataBinding()

        observeQna()
    }

    private fun setupAppBar() {
        binding.appbarQna.setOnNavigationClick {
            findNavController().popBackStack()
        }
    }

    private fun setupDataBinding() {
        binding.onWriteAnswerButtonClick = ::navigateToWriteAnswer
        binding.onConnectButtonClick = ::navigateToConnect
        binding.onResponseAddButtonClick = ::showResponseSelectDialog
    }

    private fun navigateToWriteAnswer() {
        val questionId = viewModel.qna.value?.question?.id ?: return
        findNavController().navigateSafely(
            QnaFragmentDirections.actionQnaFragmentToWriteAnswerFragment(questionId)
        )
    }

    private fun navigateToConnect() {
        findNavController().navigateSafely(QnaFragmentDirections.actionQnaFragmentToConnectFragment())
    }

    private fun showResponseSelectDialog() {
        ResponseSelectDialog().show(childFragmentManager, ResponseSelectDialog.TAG)
    }

    private fun observeQna() {
        repeatOnStarted {
            viewModel.qna
                .filterNotNull()
                .collect(::handleQna)
        }
    }

    private fun handleQna(qna: Qna) {
        binding.qna = qna

        updateLoginUserAnswer(qna)
        updateWriteAnswerButton(qna)
        updateFianceAnswer(qna)
        updateFianceResponse(qna)
        updateResponseResult(qna)
        updateLoginUserResponse(qna)
    }

    private fun updateLoginUserAnswer(qna: Qna) {
        binding.tvQnaLoginUserAnswer.text = when (qna) {
            is UnconnectedQna -> qna.loginUserAnswer.value
            is UnfinishedAnswerQna -> if (qna.loginUserAnswer == null) {
                getString(R.string.qna_request_answer_message_format, qna.fiance.name)
            } else {
                qna.loginUserAnswer!!.value
            }

            is UnfinishedResponseQna -> qna.loginUserAnswer.value
            is FinishedQna -> qna.loginUserAnswer.value
        }
    }

    private fun updateWriteAnswerButton(qna: Qna) {
        binding.tvQnaWriteAnswerButton.isVisible =
            qna is UnfinishedAnswerQna && qna.loginUserAnswer == null
    }

    private fun updateFianceAnswer(qna: Qna) {
        binding.tvQnaFianceAnswerLabel.text = when (qna) {
            is UnconnectedQna -> getString(R.string.qna_unconnected_answer_label)
            is UnfinishedAnswerQna -> getString(R.string.qna_answer_label_format, qna.fiance.name)
            is UnfinishedResponseQna -> getString(R.string.qna_answer_label_format, qna.fiance.name)
            is FinishedQna -> getString(R.string.qna_answer_label_format, qna.fiance.name)
        }

        binding.tvQnaFianceAnswer.text = when (qna) {
            is UnconnectedQna -> getString(R.string.qna_unconnected_answer_description)
            is UnfinishedAnswerQna -> if (qna.fianceAnswer == null) {
                getString(R.string.qna_fiance_answer_waiting_description)
            } else {
                getString(R.string.qna_fiance_answer_locked_description)
            }

            is UnfinishedResponseQna -> qna.fianceAnswer.value
            is FinishedQna -> qna.fianceAnswer.value
        }
    }

    private fun updateFianceResponse(qna: Qna) {
        binding.ivQnaFianceResponse.setImageDrawable(
            when (qna) {
                is UnconnectedQna -> ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.img_qna_lock_response
                )

                is UnfinishedAnswerQna -> ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.img_qna_lock_response
                )

                is UnfinishedResponseQna -> when {
                    qna.loginUserResponse == null -> ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.img_qna_lock_response
                    )

                    qna.fianceResponse == null -> ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.img_qna_waiting_response
                    )

                    else -> qna.fianceResponse!!.asDrawable()
                }

                is FinishedQna -> qna.fianceResponse.asDrawable()
            }
        )

        binding.tvQnaFianceResponseLabel.text = when (qna) {
            is UnconnectedQna -> getString(R.string.qna_locked_response_label)
            is UnfinishedAnswerQna -> getString(R.string.qna_locked_response_label)
            is UnfinishedResponseQna -> when {
                qna.loginUserResponse == null -> getString(R.string.qna_locked_response_label)
                qna.fianceResponse == null -> getString(R.string.qna_waiting_response_label)
                else -> qna.fianceResponse!!.asText()
            }

            is FinishedQna -> qna.fianceResponse.asText()
        }
    }

    private fun updateResponseResult(qna: Qna) {
        binding.ivQnaResponseResult.setImageDrawable(
            when (qna) {
                is UnconnectedQna -> ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.img_qna_lock_response
                )

                is UnfinishedAnswerQna -> ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.img_qna_lock_response
                )

                is UnfinishedResponseQna -> ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.img_qna_lock_response
                )

                is FinishedQna -> qna.responseResult.asDrawable()
            }
        )

        binding.tvQnaResponseResultLabel.text = when (qna) {
            is UnconnectedQna -> getString(R.string.qna_locked_response_label)
            is UnfinishedAnswerQna -> getString(R.string.qna_locked_response_label)
            is UnfinishedResponseQna -> getString(R.string.qna_locked_response_label)
            is FinishedQna -> qna.responseResult.asText()
        }
    }

    private fun updateLoginUserResponse(qna: Qna) {
        binding.ivQnaLoginUserResponse.setImageDrawable(
            when (qna) {
                is UnconnectedQna -> ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.img_qna_lock_response
                )

                is UnfinishedAnswerQna -> ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.img_qna_lock_response
                )

                is UnfinishedResponseQna -> if (qna.loginUserResponse == null) {
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.img_qna_login_user_response_add
                    )
                } else {
                    qna.loginUserResponse!!.asDrawable()
                }

                is FinishedQna -> qna.loginUserResponse.asDrawable()
            }
        )

        binding.ivQnaLoginUserResponse.isEnabled =
            qna is UnfinishedResponseQna && qna.loginUserResponse == null

        binding.ivQnaLoginUserResponseIndicator.isVisible = when (qna) {
            is UnconnectedQna -> false
            is UnfinishedAnswerQna -> false
            is UnfinishedResponseQna -> qna.loginUserResponse != null
            is FinishedQna -> true
        }

        binding.tvQnaLoginUserResponseLabel.text = when (qna) {
            is UnconnectedQna -> getString(R.string.qna_locked_response_label)
            is UnfinishedAnswerQna -> getString(R.string.qna_locked_response_label)
            is UnfinishedResponseQna -> if (qna.loginUserResponse == null) "" else qna.loginUserResponse!!.asText()
            is FinishedQna -> qna.loginUserResponse.asText()
        }
    }

    private fun Response.asDrawable(): Drawable? {
        val drawableId = when (this) {
            Response.GOOD -> R.drawable.img_qna_good_response
            Response.BETTER_KNOW -> R.drawable.img_qna_better_know_response
            Response.LETS_TALK -> R.drawable.img_qna_lets_talk_response
            Response.LETS_FIND -> R.drawable.img_qna_lets_find_response
        }
        return ContextCompat.getDrawable(requireContext(), drawableId)
    }

    private fun Response.asText(): String = when (this) {
        Response.GOOD -> getString(R.string.qna_good_response_label)
        Response.BETTER_KNOW -> getString(R.string.qna_better_know_response_label)
        Response.LETS_TALK -> getString(R.string.qna_lets_talk_response_label)
        Response.LETS_FIND -> getString(R.string.qna_lets_find_response_label)
    }

    private fun ResponseResult.asDrawable(): Drawable? {
        val drawableId = when (this) {
            ResponseResult.DOING_WELL -> R.drawable.img_qna_doing_well_response_result
            ResponseResult.MORE_TALK -> R.drawable.img_qna_more_talk_response_result
            ResponseResult.MORE_FIND -> R.drawable.img_qna_more_find_response_result
        }
        return ContextCompat.getDrawable(requireContext(), drawableId)
    }

    private fun ResponseResult.asText(): String = when (this) {
        ResponseResult.DOING_WELL -> getString(R.string.qna_doing_well_response_result_label)
        ResponseResult.MORE_TALK -> getString(R.string.qna_more_talk_response_result_label)
        ResponseResult.MORE_FIND -> getString(R.string.qna_more_find_response_result_label)
    }
}
