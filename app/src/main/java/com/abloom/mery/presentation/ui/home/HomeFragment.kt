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
import com.abloom.mery.presentation.common.base.BaseFragment
import com.abloom.mery.presentation.common.util.repeatOnStarted
import com.abloom.mery.presentation.ui.home.qnasrecyclerview.QnaAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

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

        checkLoginState()

        observeMainEvent()
        observeQnas()
        observeLoginUser()
    }

    private fun checkLoginState() {
        if (homeViewModel.isLogin.value == null)
            showLoginDialog()
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

    private fun showLoginDialog() {
        val bottomSheetFragment = LoginDialogFragment()
        bottomSheetFragment.show(childFragmentManager, LoginDialogFragment().tag)
    }

    private fun observeMainEvent() {
        repeatOnStarted {
            mainViewModel.loginEvent
                .combine(homeViewModel.isLogin) { _, isLogin -> isLogin }
                .filterNotNull()
                .collect(::showLoginDialog)
        }
    }

    private fun showLoginDialog(isLogin: Boolean) {
        if (!isLogin) {
            val bottomSheetFragment = LoginDialogFragment()
            bottomSheetFragment.show(childFragmentManager, LoginDialogFragment().tag)
        }
    }

    private fun observeQnas() {
        repeatOnStarted { homeViewModel.qnas.collect(qnaAdapter::submitList) }
    }

    private fun observeLoginUser() {
        repeatOnStarted { homeViewModel.loginUser.collect(::updateUserImage) }
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
