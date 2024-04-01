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
import sh.tyy.wheelpicker.DatePickerView
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
            onDayClick = { day ->
                if (day.position == DayPosition.MonthDate) {
                    marriageDateChangeViewModel.selectDate(day.date)
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
        setupMonthPicker()

        observeSelectedDate()
        observeIsCalendarState()
    }

    private fun FrameLayout.setupBackground() {
        background = null
        setPadding(8.dp)
    }

    private fun FrameLayout.setupBehavior() {
        val behavior = BottomSheetBehavior.from(this)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
        behavior.isHideable = false
    }

    private fun setupDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = marriageDateChangeViewModel
        binding.onCompleteButtonClick = ::handleCompleteButtonClick
    }

    private fun handleCompleteButtonClick() {
        if (!marriageDateChangeViewModel.isCalendarState.value) {
            marriageDateChangeViewModel.toggleCalendarState()
            return
        }
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
        binding.cvMarriagedatechangedialogCalender.dayBinder = calendarDayAdapter

        binding.cvMarriagedatechangedialogCalender.monthScrollListener = { date ->
            marriageDateChangeViewModel.setVisibleMonth(date.yearMonth)
        }

        updateCalendar(YearMonth.now())
    }

    private fun updateCalendar(visibleMonth: YearMonth) {
        val startMonth = visibleMonth.minusMonths(SCROLLABLE_MONTH_COUNT)
        val endMonth = visibleMonth.plusMonths(SCROLLABLE_MONTH_COUNT)

        binding.cvMarriagedatechangedialogCalender.setup(
            startMonth = startMonth,
            endMonth = endMonth,
            firstDayOfWeek = daysOfWeek().first()
        )

        binding.cvMarriagedatechangedialogCalender.scrollToMonth(visibleMonth)
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

    private fun setupMonthPicker() {
        binding.dpvMarriagedatechangedialogMonthPicker.mode = DatePickerView.Mode.YEAR_MONTH
        val visibleMonth = marriageDateChangeViewModel.visibleMonth.value
        binding.dpvMarriagedatechangedialogMonthPicker.setDate(
            year = visibleMonth.year,
            month = visibleMonth.monthValue,
            day = 1
        )
    }

    private fun observeSelectedDate() {
        repeatOnStarted {
            marriageDateChangeViewModel.selectedDate
                .filterNotNull()
                .collect { selectedDate ->
                    calendarDayAdapter.selectedDate = selectedDate
                    binding.cvMarriagedatechangedialogCalender.notifyMonthChanged(selectedDate.yearMonth)
                }
        }
    }

    private fun observeIsCalendarState() {
        repeatOnStarted {
            marriageDateChangeViewModel.isCalendarState
                .collect { isCalendarState ->
                    if (isCalendarState) {
                        updateCalendarState()
                    } else {
                        updateYearMonthSpinner()
                    }
                }
        }
    }

    private fun updateCalendarState() {
        val year = binding.dpvMarriagedatechangedialogMonthPicker.year
        val month = binding.dpvMarriagedatechangedialogMonthPicker.month
        if (!isValidDate(year, month)) return
        val visibleMonth = YearMonth.of(year, month)
        updateCalendar(visibleMonth)
        marriageDateChangeViewModel.setVisibleMonth(visibleMonth)
    }

    private fun isValidDate(year: Int, month: Int): Boolean = year > 0 && month in 1..12

    private fun updateYearMonthSpinner() {
        val visibleMonth = marriageDateChangeViewModel.visibleMonth.value
        binding.dpvMarriagedatechangedialogMonthPicker.setDate(
            year = visibleMonth.year,
            month = visibleMonth.monthValue,
            day = 1
        )
    }

    companion object {

        private const val SCROLLABLE_MONTH_COUNT = 100L
    }
}
