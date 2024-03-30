package com.abloom.mery.presentation.ui.createqna

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentCreateQnaBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import com.abloom.mery.presentation.common.util.repeatOnStarted
import com.abloom.mery.presentation.common.view.setOnNavigationClick
import com.abloom.mery.presentation.ui.category.CategoryArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class CreateQnaFragment : BaseFragment<FragmentCreateQnaBinding>(R.layout.fragment_create_qna) {

    private val viewModel: CreateQnaViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appbarCreateQna.setOnNavigationClick {
            findNavController().popBackStack()
        }
        setUpDataBinding()
        setUpListener()
    }

    private fun setUpDataBinding() {
        binding.viewModel = viewModel
        binding.goWriteAnswerOnClick = ::navigateToWriteAnswer
    }

    private fun setUpListener() {
        binding.ivEconomy.setOnClickListener { goCategoryFragment(CategoryArgs.FINANCE) }
        binding.ivCommunication.setOnClickListener { goCategoryFragment(CategoryArgs.COMMUNICATION) }
        binding.ivValues.setOnClickListener { goCategoryFragment(CategoryArgs.VALUES) }
        binding.ivLife.setOnClickListener { goCategoryFragment(CategoryArgs.LIFESTYLE) }
        binding.ivChildren.setOnClickListener { goCategoryFragment(CategoryArgs.CHILDREN) }
        binding.ivFamily.setOnClickListener { goCategoryFragment(CategoryArgs.FAMILY) }
        binding.ivMarried.setOnClickListener { goCategoryFragment(CategoryArgs.SEX) }
        binding.ivHealth.setOnClickListener { goCategoryFragment(CategoryArgs.HEALTH) }
        binding.ivWedding.setOnClickListener { goCategoryFragment(CategoryArgs.WEDDING) }
        binding.ivFuture.setOnClickListener { goCategoryFragment(CategoryArgs.FUTURE) }
        binding.ivPresent.setOnClickListener { goCategoryFragment(CategoryArgs.PRESENT) }
        binding.ivPast.setOnClickListener { goCategoryFragment(CategoryArgs.PAST) }
    }

    private fun navigateToWriteAnswer() {
        val id = viewModel.todayRecommendationQuestion.value?.id ?: return
        findNavController().navigate(
            CreateQnaFragmentDirections.actionGlobalWriteAnswerFragment(id)
        )
    }

    private fun goCategoryFragment(category: CategoryArgs) {
        val action = CreateQnaFragmentDirections.actionCreateQnaFragmentToCategoryFragment(category)
        findNavController().navigate(action)
    }
}
