package com.abloom.mery.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentHomeBinding
import com.abloom.mery.presentation.MainViewModel
import com.abloom.mery.presentation.common.base.BaseFragment
import com.abloom.mery.presentation.common.util.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDataBinding()

        observeMainEvent()
    }

    private fun setupDataBinding() {
        binding.viewModel = homeViewModel
    }

    private fun observeMainEvent() {
        repeatOnStarted {
            mainViewModel.loginEvent
                .combine(homeViewModel.isLogin) { _, isLogin -> isLogin }
                .filterNotNull()
                .collect(::handleLoginEvent)
        }
    }

    private fun handleLoginEvent(isLogin: Boolean) {
        if (!isLogin) {
            val bottomSheetFragment = LoginDialogFragment()
            bottomSheetFragment.show(childFragmentManager, LoginDialogFragment().tag)
        }
    }
}
