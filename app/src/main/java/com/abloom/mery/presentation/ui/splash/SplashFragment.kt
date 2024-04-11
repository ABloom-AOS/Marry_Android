package com.abloom.mery.presentation.ui.splash

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentSplashBinding
import com.abloom.mery.presentation.common.base.NavigationFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : NavigationFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigateToHomeLazily()
    }

    private fun navigateToHomeLazily() {
        lifecycleScope.launch {
            delay(SPLASH_DURATION)
            if (isDeepLinked()) return@launch
            findNavController().navigateSafely(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
        }
    }

    private fun isDeepLinked(): Boolean =
        findNavController().currentDestination?.id != R.id.splashFragment

    companion object {

        private const val SPLASH_DURATION = 1_500L
    }
}
