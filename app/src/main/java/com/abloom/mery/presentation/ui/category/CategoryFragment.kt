package com.abloom.mery.presentation.ui.category

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.abloom.domain.question.model.Category
import com.abloom.domain.question.model.Question
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentCategoryBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import com.abloom.mery.presentation.common.util.repeatOnStarted
import com.abloom.mery.presentation.common.view.setOnNavigationClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : BaseFragment<FragmentCategoryBinding>(R.layout.fragment_category) {

    private lateinit var categoryAdapter: CategoryAdapter
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val args: CategoryFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupIsLogin()
        setCategoryAdapter()
        setupSelectedTabItem(args.category)
        setupListener()
    }

    private fun setupIsLogin() {

        repeatOnStarted {
            categoryViewModel.isLogin.collect { isLogin ->
                when (isLogin) {
                    true -> {
                        binding.clNologin.visibility = View.INVISIBLE
                    }

                    false -> {
                        binding.clNologin.visibility = View.VISIBLE
                        binding.tvLoginTag.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                    }
                }

            }
        }
    }

    private fun setupSelectedTabItem(categoryArgs: CategoryArgs) {

        when (categoryArgs) {
            CategoryArgs.FINANCE ->
                selectTabItem(
                    Category.FINANCE,
                    FINANCE_TABITEM_POSION
                )

            CategoryArgs.COMMUNICATION -> selectTabItem(
                Category.COMMUNICATION,
                COMMUNICATION_TABITEM_POSION
            )

            CategoryArgs.VALUES -> selectTabItem(
                Category.VALUES,
                VALUES_TABITEM_POSION
            )

            CategoryArgs.LIFESTYLE -> selectTabItem(
                Category.LIFESTYLE,
                LIFESTYLE_TABITEM_POSION
            )

            CategoryArgs.CHILDREN -> selectTabItem(
                Category.CHILD,
                CHILD_TABITEM_POSION
            )

            CategoryArgs.FAMILY -> selectTabItem(
                Category.FAMILY,
                FAMILY_TABITEM_POSION
            )

            CategoryArgs.SEX -> selectTabItem(
                Category.SEX, SEX_TABITEM_POSION
            )

            CategoryArgs.HEALTH -> selectTabItem(
                Category.HEALTH,
                HEALTH_TABITEM_POSION
            )

            CategoryArgs.WEDDING -> selectTabItem(
                Category.WEDDING,
                WEDDING_TABITEM_POSION
            )

            CategoryArgs.FUTURE -> selectTabItem(
                Category.FUTURE,
                FUTURE_TABITEM_POSION
            )

            CategoryArgs.PRESENT -> selectTabItem(
                Category.PRESENT,
                PRESENT_TABITEM_POSION
            )

            CategoryArgs.PAST -> selectTabItem(
                Category.PAST, PAST_TABITEM_POSION
            )
        }
    }

    private fun setupListener() {

        binding.appbarCategory.setOnNavigationClick {
            findNavController().popBackStack()
        }


        binding.tvLoginTag.setOnClickListener {
            findNavController().popBackStack(R.id.homeFragment, false)
        }

        binding.tb.onTabSelected { tab ->
            repeatOnStarted {
                categoryViewModel.questions.collect { map ->
                    when (tab.position) {
                        FINANCE_TABITEM_POSION -> {
                            map[Category.FINANCE]?.let { categoryAdapter.setData(it) }
                        }

                        COMMUNICATION_TABITEM_POSION -> {
                            map[Category.COMMUNICATION]?.let { categoryAdapter.setData(it) }
                        }

                        VALUES_TABITEM_POSION -> {
                            map[Category.VALUES]?.let { categoryAdapter.setData(it) }
                        }

                        LIFESTYLE_TABITEM_POSION -> {
                            map[Category.LIFESTYLE]?.let { categoryAdapter.setData(it) }
                        }

                        CHILD_TABITEM_POSION -> {
                            map[Category.CHILD]?.let { categoryAdapter.setData(it) }
                        }

                        FAMILY_TABITEM_POSION -> {
                            map[Category.FAMILY]?.let { categoryAdapter.setData(it) }
                        }

                        SEX_TABITEM_POSION -> {
                            map[Category.SEX]?.let { categoryAdapter.setData(it) }
                        }

                        HEALTH_TABITEM_POSION -> {
                            map[Category.HEALTH]?.let { categoryAdapter.setData(it) }
                        }

                        WEDDING_TABITEM_POSION -> {
                            map[Category.WEDDING]?.let { categoryAdapter.setData(it) }
                        }

                        FUTURE_TABITEM_POSION -> {
                            map[Category.FUTURE]?.let { categoryAdapter.setData(it) }
                        }

                        PRESENT_TABITEM_POSION -> {
                            map[Category.PRESENT]?.let { categoryAdapter.setData(it) }
                        }

                        PAST_TABITEM_POSION -> {
                            map[Category.PAST]?.let { categoryAdapter.setData(it) }
                        }
                    }
                }
            }
        }
    }

    private fun setCategoryAdapter() {
        categoryAdapter = CategoryAdapter(::onCategoryItemClick)
        val categoryManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rv.apply {
            layoutManager = categoryManager
            adapter = categoryAdapter
            scrollToPosition(0)
        }
    }

    private fun selectTabItem(category: Category, index: Int) {
        binding.tb.selectTab(binding.tb.getTabAt(index))
        lifecycleScope.launch(Dispatchers.Main) {
            binding.tb.setScrollPosition(index, 0f, false)
        }
        repeatOnStarted {
            categoryViewModel.questions.collect { map ->
                map[category]?.let { questions ->
                    categoryAdapter.setData(questions)
                }

            }
        }
    }

    private fun onCategoryItemClick(question: Question) {
        val action =
            CategoryFragmentDirections.actionGlobalWriteAnswerFragment(question.id)
        findNavController().navigate(action)
    }

    companion object {

        private const val FINANCE_TABITEM_POSION = 0
        private const val COMMUNICATION_TABITEM_POSION = 1
        private const val VALUES_TABITEM_POSION = 2
        private const val LIFESTYLE_TABITEM_POSION = 3
        private const val CHILD_TABITEM_POSION = 4
        private const val FAMILY_TABITEM_POSION = 5
        private const val SEX_TABITEM_POSION = 6
        private const val HEALTH_TABITEM_POSION = 7
        private const val WEDDING_TABITEM_POSION = 8
        private const val FUTURE_TABITEM_POSION = 9
        private const val PRESENT_TABITEM_POSION = 10
        private const val PAST_TABITEM_POSION = 11
    }
}
