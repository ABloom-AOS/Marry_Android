package com.abloom.mery.presentation.ui.createqna

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abloom.mery.MixpanelManager
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentCreateQnaBinding
import com.abloom.mery.presentation.MainViewModel
import com.abloom.mery.presentation.common.base.NavigationFragment
import com.abloom.mery.presentation.ui.category.CategoryArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateQnaFragment :
    NavigationFragment<FragmentCreateQnaBinding>(R.layout.fragment_create_qna) {

    private val createQnaViewModel: CreateQnaViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
    }

    private fun setupDataBinding() {
        binding.viewModel = createQnaViewModel
        binding.onCancelClick = { findNavController().popBackStack() }
        binding.onTodayQuestionClick = ::handleTodayQuestionClick
        binding.onCategoryClick = ::navigateToCategory
    }

    private fun navigateToCategory(category: CategoryArgs) {
        MixpanelManager.selectCategory(category.name.lowercase())
        val action = CreateQnaFragmentDirections.actionCreateQnaFragmentToCategoryFragment(category)
        findNavController().navigateSafely(action)
    }

    private fun handleTodayQuestionClick(questionId: Long) {
        lifecycleScope.launch {
            val isLogin = createQnaViewModel.isLogin.value ?: return@launch
            if (isLogin) {
                MixpanelManager.recommendTodayQuestion(questionId)
                navigateToWriteAnswer(questionId)
            } else {
                mainViewModel.dispatchLoginEvent()
                findNavController().popBackStack()
            }
        }
    }

    private fun navigateToWriteAnswer(questionId: Long) {
        findNavController().navigateSafely(
            CreateQnaFragmentDirections.actionGlobalWriteAnswerFragment(questionId)
        )
    }
}
