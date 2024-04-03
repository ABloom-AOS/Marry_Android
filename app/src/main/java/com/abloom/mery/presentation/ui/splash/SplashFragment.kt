package com.abloom.mery.presentation.ui.splash

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentSplashBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateToHomeLazily()
    }

    private fun navigateToHomeLazily() {
        lifecycleScope.launch {
            delay(SPLASH_DURATION)
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
        }
    }

    companion object {

        private const val SPLASH_DURATION = 1_500L
    }
}
