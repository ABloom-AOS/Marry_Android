package com.abloom.mery.presentation.ui.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abloom.mery.MixpanelManager
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentCategoryBinding
import com.abloom.mery.presentation.MainViewModel
import com.abloom.mery.presentation.common.base.NavigationFragment
import com.abloom.mery.presentation.common.extension.repeatOnStarted
import com.abloom.mery.presentation.ui.category.recyclerview.QuestionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class CategoryFragment : NavigationFragment<FragmentCategoryBinding>(R.layout.fragment_category) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()

    private val questionAdapter: QuestionAdapter by lazy { QuestionAdapter(::navigateToWriteAnswer) }

    private fun navigateToWriteAnswer(questionId: Long) {
        MixpanelManager.selectQuestion(
            categoryViewModel.category.value.name.toLowerCase(Locale.ROOT),
            questionId
        )
        val action = CategoryFragmentDirections.actionGlobalWriteAnswerFragment(questionId)
        findNavController().navigateSafely(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupQuestionRecyclerView()
        setupCategoryTab()

        observeCategory()
        observeQuestions()
    }

    private fun setupDataBinding() {
        binding.viewModel = categoryViewModel
        binding.onUpButtonClick = { findNavController().popBackStack() }
        binding.onLoginButtonClick = ::handleLoginButtonClick
    }

    private fun handleLoginButtonClick() {
        mainViewModel.dispatchLoginEvent()
        findNavController().popBackStack(R.id.homeFragment, false)
    }

    private fun setupQuestionRecyclerView() {
        binding.rv.adapter = questionAdapter
    }

    private fun setupCategoryTab() {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.tb.setScrollPosition(categoryViewModel.category.value.ordinal, 0f, false)
        }
        binding.tb.onTabSelected { tab ->
            categoryViewModel.selectCategory(CategoryArgs.entries[tab.position])
        }
    }

    private fun observeCategory() {
        repeatOnStarted {
            categoryViewModel.category.collect { category ->
                binding.tb.selectTab(binding.tb.getTabAt(category.ordinal))
            }
        }
    }

    private fun observeQuestions() {
        repeatOnStarted { categoryViewModel.currentQuestions.collect(questionAdapter::submitList) }
    }
}
