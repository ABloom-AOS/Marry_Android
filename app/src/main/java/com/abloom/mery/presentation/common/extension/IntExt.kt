package com.abloom.mery.presentation.common.extension

import com.abloom.mery.presentation.MainActivity.Companion.FIXED_DENSITY
import kotlin.math.roundToInt

val Int.dp: Int
    get() {
        return (this * FIXED_DENSITY).roundToInt()
    }
