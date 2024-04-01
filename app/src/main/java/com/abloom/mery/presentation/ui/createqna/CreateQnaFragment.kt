package com.abloom.mery.presentation.ui.createqna

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentCreateQnaBinding
import com.abloom.mery.presentation.MainViewModel
import com.abloom.mery.presentation.common.base.BaseFragment
import com.abloom.mery.presentation.common.view.setOnNavigationClick
import com.abloom.mery.presentation.ui.category.CategoryArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateQnaFragment : BaseFragment<FragmentCreateQnaBinding>(R.layout.fragment_create_qna) {

    private val createQnaViewModel: CreateQnaViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appbarCreateQna.setOnNavigationClick {
            findNavController().popBackStack()
        }
        setUpDataBinding()
        setUpListener()
    }

    private fun setUpDataBinding() {
        binding.viewModel = createQnaViewModel
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

        binding.clQuestion.setOnClickListener {
            when (createQnaViewModel.isLogin.value) {
                true -> {
                    createQnaViewModel.todayRecommendationQuestion.value?.let {
                        findNavController().navigate(
                            CreateQnaFragmentDirections.actionGlobalWriteAnswerFragment(it.id)
                        )
                    }
                }

                false -> {
                    mainViewModel.dispatchLoginEvent()
                    findNavController().popBackStack(R.id.homeFragment, false)
                }

                null -> return@setOnClickListener
            }
        }
    }

    private fun goCategoryFragment(category: CategoryArgs) {
        val action = CreateQnaFragmentDirections.actionCreateQnaFragmentToCategoryFragment(category)
        findNavController().navigate(action)
    }
}
