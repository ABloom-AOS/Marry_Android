package com.abloom.mery.presentation.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentMarryDateBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import sh.tyy.wheelpicker.DatePickerView
import java.time.LocalDate

@AndroidEntryPoint
class MarryDateFragment : BaseFragment<FragmentMarryDateBinding>(R.layout.fragment_marry_date) {

    private val viewModel: SignUpViewModel by viewModels({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initSavedDate()
    }

    private fun initListener() {
        binding.datePicker.setWheelListener(object : DatePickerView.Listener {
            override fun didSelectData(year: Int, month: Int, day: Int) {
                if (isValidDate(year, month + 1, day)) {
                    val marriageDate = LocalDate.of(year, month + 1, day)
                    viewModel.selectMarriageDate(marriageDate)
                }
            }
        })
    }

    fun isValidDate(year: Int, month: Int, day: Int): Boolean {
        if (month !in 1..12) return false
        val maxDaysInMonth = when (month) {
            2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }
        return day in 1..maxDaysInMonth
    }

    private fun initSavedDate() {
        val dayValue = viewModel.selectedMarriage.value
        binding.datePicker.setDate(dayValue.year, dayValue.monthValue - 1, dayValue.dayOfMonth)
    }

}
