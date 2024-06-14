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
import com.abloom.mery.presentation.ui.webview.WebViewUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CategoryFragment : NavigationFragment<FragmentCategoryBinding>(R.layout.fragment_category) {

    @Inject
    lateinit var mixpanelManager: MixpanelManager

    private val mainViewModel: MainViewModel by activityViewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()

    private val questionAdapter: QuestionAdapter by lazy { QuestionAdapter(::navigateToWriteAnswer) }

    private fun navigateToWriteAnswer(questionId: Long) {
        mainViewModel.wasClosedQuestionFactoryPopup = true
        mixpanelManager.selectQuestion(
            category = categoryViewModel.category.value.name.lowercase(),
            questionId = questionId
        )
        val action = CategoryFragmentDirections.actionGlobalWriteAnswerFragment(questionId, false)
        findNavController().navigateSafely(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()
        setupQuestionRecyclerView()
        setupCategoryTab()

        observeCategory()
        observeQuestions()

        showPopUpDialogIfNeed()
    }

    private fun setupDataBinding() {
        binding.viewModel = categoryViewModel
        binding.onUpButtonClick = { findNavController().popBackStack() }
        binding.onLoginButtonClick = ::handleLoginButtonClick
        binding.onNavigateQuestionFactoryButtonClick = ::navigateToQuestionFactoryWebView
        binding.onPopUpDialogCloseButtonClick = ::closePopUpDialog
    }

    private fun handleLoginButtonClick() {
        mainViewModel.dispatchLoginEvent()
        findNavController().popBackStack(R.id.homeFragment, false)
    }

    private fun navigateToQuestionFactoryWebView() {
        findNavController().navigateSafely(
            CategoryFragmentDirections.actionCategoryFragmentToWebViewFromCategoryFragment(
                WebViewUrl.QUESTION_FACTORY
            )
        )
    }

    private fun closePopUpDialog() {
        mainViewModel.wasClosedQuestionFactoryPopup = true
        hidePopupDialog()
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

    private fun showPopUpDialogIfNeed() {
        lifecycleScope.launch {
            delay(DIALOG_DISPLAY_DELAY_TIME)
            if (checkPopUpDialogCondition()) {
                showPopupDialog()
            }
        }
    }

    private fun showPopupDialog() {
        binding.makeQuestionPopUpDialog.visibility = View.VISIBLE
        binding.popUpCloseBtn.visibility = View.VISIBLE
    }

    private fun hidePopupDialog() {
        binding.makeQuestionPopUpDialog.visibility = View.INVISIBLE
        binding.popUpCloseBtn.visibility = View.INVISIBLE
    }

    private fun checkPopUpDialogCondition() =
        categoryViewModel.isLogin.value && !mainViewModel.wasClosedQuestionFactoryPopup

    companion object {
        private const val DIALOG_DISPLAY_DELAY_TIME = 5000L
    }
}
