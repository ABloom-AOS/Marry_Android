package com.abloom.mery.presentation.ui.profilemenu.marriagedatechange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.setPadding
import androidx.fragment.app.viewModels
import com.abloom.mery.databinding.DialogMarriageDateChangeBinding
import com.abloom.mery.presentation.common.util.dp
import com.abloom.mery.presentation.common.util.repeatOnStarted
import com.abloom.mery.presentation.ui.profilemenu.ProfileMenuViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.coroutines.flow.filterNotNull
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class MarriageDateChangeDialog : BottomSheetDialogFragment() {

    private val profileMenuViewModel: ProfileMenuViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private val marriageDateChangeViewModel: MarriageDateChangeViewModel by viewModels()

    private var _binding: DialogMarriageDateChangeBinding? = null
    private val binding get() = _binding!!

    private val bottomSheet: FrameLayout by lazy {
        val parentView = binding.root.parent as View
        parentView.findViewById(com.google.android.material.R.id.design_bottom_sheet)
    }

    private val calendarDayAdapter: CalendarDayAdapter by lazy {
        CalendarDayAdapter(
            selectedDate = marriageDateChangeViewModel.selectedDate.value
                ?: throw IllegalStateException("선택된 날짜를 초기화하기 전에 호출했습니다."),
            onDayClick = {
                if (it.position == DayPosition.MonthDate) {
                    marriageDateChangeViewModel.selectDate(it.date)
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMarriageDateChangeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheet.setupBackground()
        bottomSheet.setupBehavior()
        setupDataBinding()
        setupSelectedDate()
        setupCalendar()
        setupDayOfWeekHeader()

        observeSelectedDate()
    }

    private fun FrameLayout.setupBackground() {
        background = null
        setPadding(8.dp)
    }

    private fun FrameLayout.setupBehavior() {
        val behavior = BottomSheetBehavior.from(this)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }

    private fun setupDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = marriageDateChangeViewModel
        binding.onCompleteButtonClick = ::handleCompleteButtonClick
    }

    private fun handleCompleteButtonClick() {
        val selectedDate = marriageDateChangeViewModel.selectedDate.value ?: return
        profileMenuViewModel.changeMarriageDate(selectedDate)
        dismiss()
    }

    private fun setupSelectedDate() {
        val marriageDate = profileMenuViewModel.loginUser.value?.marriageDate
            ?: throw AssertionError("로그인하지 않은 상태에서 결혼 예정일 변경 다이얼로그를 열었을 리 없습니다.")
        marriageDateChangeViewModel.selectDate(marriageDate)
    }

    private fun setupCalendar() {
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)

        binding.cvMarriagedatechangedialogCalender.dayBinder = calendarDayAdapter

        binding.cvMarriagedatechangedialogCalender.setup(
            startMonth = startMonth,
            endMonth = endMonth,
            firstDayOfWeek = daysOfWeek().first()
        )

        binding.cvMarriagedatechangedialogCalender.monthScrollListener =
            { marriageDateChangeViewModel.setVisibleMonth(it.yearMonth) }

        binding.cvMarriagedatechangedialogCalender.scrollToMonth(currentMonth)
    }

    private fun setupDayOfWeekHeader() {
        binding.tvMarriagedatechangedialogSundayHeader.text =
            DayOfWeek.SUNDAY.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        binding.tvMarriagedatechangedialogMondayHeader.text =
            DayOfWeek.MONDAY.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        binding.tvMarriagedatechangedialogTuesdayHeader.text =
            DayOfWeek.TUESDAY.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        binding.tvMarriagedatechangedialogWednesdayHeader.text =
            DayOfWeek.WEDNESDAY.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        binding.tvMarriagedatechangedialogThursdayHeader.text =
            DayOfWeek.THURSDAY.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        binding.tvMarriagedatechangedialogFridayHeader.text =
            DayOfWeek.FRIDAY.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        binding.tvMarriagedatechangedialogSaturdayHeader.text =
            DayOfWeek.SATURDAY.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }

    private fun observeSelectedDate() {
        repeatOnStarted {
            marriageDateChangeViewModel.selectedDate.filterNotNull().collect {
                calendarDayAdapter.selectedDate = it
                binding.cvMarriagedatechangedialogCalender.notifyMonthChanged(it.yearMonth)
            }
        }
    }
}
