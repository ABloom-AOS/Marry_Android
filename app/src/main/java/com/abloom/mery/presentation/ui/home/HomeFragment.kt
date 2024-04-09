package com.abloom.mery.presentation.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abloom.domain.user.model.Sex
import com.abloom.domain.user.model.User
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentHomeBinding
import com.abloom.mery.presentation.MainViewModel
import com.abloom.mery.presentation.common.base.NavigationFragment
import com.abloom.mery.presentation.common.extension.repeatOnStarted
import com.abloom.mery.presentation.ui.home.qnasrecyclerview.QnaAdapter
import com.abloom.mery.presentation.ui.signup.asArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class HomeFragment : NavigationFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    private val qnaAdapter: QnaAdapter by lazy { QnaAdapter(onQnaClick = ::navigateToQna) }

    private fun navigateToQna(questionId: Long) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToQnaFragment(questionId)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupQnaRecyclerView()
        setupDataBinding()

        observeLoginEvent()
        observeHomeEvent()
        observeQnas()
        observeLoginUser()
    }

    private fun setupQnaRecyclerView() {
        binding.rvHomeQnas.adapter = qnaAdapter
    }

    private fun setupDataBinding() {
        binding.viewModel = homeViewModel
        binding.onProfileMenuButtonClick = ::navigateToProfileMenu
        binding.onCreateQnaButtonClick = ::navigateToCreateQna
    }

    private fun navigateToProfileMenu() {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileMenuFragment())
    }

    private fun navigateToCreateQna() {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCreateQna())
    }

    private fun observeLoginEvent() {
        repeatOnStarted {
            mainViewModel.loginEvent
                .combine(homeViewModel.isLogin) { _, isLogin -> isLogin }
                .filterNotNull()
                .collect { isLogin -> if (!isLogin) showLoginDialog() }
        }
    }

    private fun showLoginDialog() {
        val bottomSheetFragment = LoginDialogFragment()
        bottomSheetFragment.show(childFragmentManager, LoginDialogFragment().tag)
    }

    private fun observeHomeEvent() {
        repeatOnStarted {
            homeViewModel.event.collect { event ->
                when (event) {
                    is HomeEvent.LoginFail -> handleLoginFail(event)
                }
            }
        }
    }

    private fun handleLoginFail(event: HomeEvent.LoginFail) {
        findNavController().navigateSafely(
            HomeFragmentDirections.actionHomeFragmentToSignUpFragment(event.authentication.asArgs())
        )
    }

    private fun observeQnas() {
        repeatOnStarted { homeViewModel.qnas.collect(qnaAdapter::submitList) }
    }

    private fun observeLoginUser() {
        repeatOnStarted { homeViewModel.loginUser.collect(::handleLoginUser) }
    }

    private fun handleLoginUser(uiState: UserUiState) {
        when (uiState) {
            UserUiState.Loading -> {}
            is UserUiState.Login -> bindUser(uiState.user)
            UserUiState.NotLogin -> bindUser(null)
        }
    }

    private fun bindUser(user: User?) {
        binding.loginUser = user
        updateUserImage(user)
    }

    private fun updateUserImage(loginUser: User?) {
        val drawableRes = when {
            loginUser == null -> R.drawable.img_profilemenu_not_login_user
            loginUser.sex == Sex.MALE -> R.drawable.img_all_groom
            loginUser.sex == Sex.FEMALE -> R.drawable.img_all_bride
            else -> throw AssertionError("여기까지 올 리 없음.")
        }
        val drawable = ContextCompat.getDrawable(requireContext(), drawableRes)
        binding.ivHomeUserIcon.setImageDrawable(drawable)
    }
}
