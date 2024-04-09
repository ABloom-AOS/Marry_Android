package com.abloom.mery.presentation.common.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

abstract class NavigationFragment<V : ViewDataBinding>(
    @LayoutRes layoutResId: Int,
) : BaseFragment<V>(layoutResId) {

    private lateinit var destination: NavDestination

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        destination = findNavController().currentDestination!!
    }

    protected fun NavController.navigateSafely(directions: NavDirections) {
        if (currentDestination != destination) return
        navigate(directions)
    }
}
