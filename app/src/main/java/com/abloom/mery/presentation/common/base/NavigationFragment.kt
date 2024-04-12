package com.abloom.mery.presentation.common.base

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.NavDirections

abstract class NavigationFragment<V : ViewDataBinding>(
    @LayoutRes layoutResId: Int,
) : BaseFragment<V>(layoutResId) {

    protected fun NavController.navigateSafely(directions: NavDirections) =
        runCatching { navigate(directions) }
}
