package com.abloom.mery.presentation.ui.profilemenu

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import com.abloom.mery.R
import com.abloom.mery.databinding.DialogProfileDetailMenuBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate

class ProfileDetailMenuDialog : BottomSheetDialogFragment() {

    private val viewModel: ProfileMenuViewModel by viewModels(ownerProducer = { requireParentFragment() })

    private var _binding: DialogProfileDetailMenuBinding? = null
    private val binding get() = _binding!!

    private val bottomSheet: FrameLayout by lazy {
        val parentView = binding.root.parent as View
        parentView.findViewById(com.google.android.material.R.id.design_bottom_sheet)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogProfileDetailMenuBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheet.setupBackground()
        bottomSheet.setupBehavior()
        setupDataBinding()

        // by viewModels를 사용하여 뷰모델을 초기화하면 viewModel 객체가 사용되는 시점에 초기화 되는데,
        // DatePicker에서 viewModel을 사용하는 시점이 ProfileDetailMenuDialog가 detach 된 이후라서
        // viewModel 객체를 초기화할 수 없습니다. 부모 프래그먼트가 없기 때문이죠.
        // 그래서 의미없어 보이는 코드를 호출했습니다.
        // 이 부분에 대해 더 나은 의견이 있다면 알려주시면 감사하겠습니다.
        viewModel
    }

    private fun FrameLayout.setupBackground() {
        background = null
    }

    private fun FrameLayout.setupBehavior() {
        val behavior = BottomSheetBehavior.from(this)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }

    private fun setupDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.onNameChangeButtonClick = ::handleNameChangeButtonClick
        binding.onMarriageDateChangeButtonClick = ::handleMarriageDateChangeButtonClick
        binding.onCancelButtonClick = ::dismiss
    }

    private fun handleNameChangeButtonClick() {
        NameChangeDialog().show(parentFragmentManager, null)
        dismiss()
    }

    private fun handleMarriageDateChangeButtonClick() {
        showDatePickerDialog()
        dismiss()
    }

    private fun showDatePickerDialog() {
        val currentDate = LocalDate.now()
        DatePickerDialog(
            requireContext(),
            R.style.DatePickerDialogTheme,
            ::changeMarriageDate,
            currentDate.year,
            currentDate.monthValue - 1,
            currentDate.dayOfMonth
        ).show()
    }

    private fun changeMarriageDate(view: View, year: Int, month: Int, day: Int) {
        val date = LocalDate.of(year, month, day)
        viewModel.changeMarriageDate(date)
    }
}
