package com.colinjp.inblo.android.presentation.ui.calendar

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.databinding.CalendarDayBinding
import com.colinjp.inblo.android.databinding.CalendarHeaderBinding
import com.colinjp.inblo.android.databinding.FragmentCalendarBinding
import com.colinjp.inblo.android.domain.model.EventType
import com.colinjp.inblo.android.domain.util.UserPreferences
import com.colinjp.inblo.android.domain.util.daysOfWeekFromLocale
import com.colinjp.inblo.android.domain.util.parseEventType
import com.colinjp.inblo.android.presentation.ui.calendar.dialogs.AddEventDialogFragment
import com.colinjp.inblo.android.presentation.ui.calendar.dialogs.ScheduleDialogFragment
import com.colinjp.inblo.android.presentation.ui.horse_list.SortOrder
import com.colinjp.inblo.domain.model.Event
import com.colinjp.inblo.domain.model.Horse
import com.colinjp.inblo.util.DataState
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.Size
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.time.LocalDate

import java.time.YearMonth
import java.time.format.DateTimeFormatter

import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@AndroidEntryPoint
class CalendarFragment: Fragment(R.layout.fragment_calendar) {

    private var calendarBinding: FragmentCalendarBinding? = null

    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val monthFormatter = DateTimeFormatter.ofPattern("MM")
    private val yearFormatter = DateTimeFormatter.ofPattern("yyyy")

    private var selectedDate: LocalDate? = null
    private lateinit var eventsAdapter: ScheduleListAdapter
    private var events: List<Event> = listOf()
    private val calendarViewModel by activityViewModels<CalendarViewModel>()
    private var eventOrder: Map<Int,Event> = HashMap()

    @Inject
    lateinit var userPreferencesFlow: Flow<UserPreferences>

    companion object {
        const val TAG_DIALOG_ADD_EVENT = "TAG_DIALOG_ADD_EVENT"
        const val TAG_DIALOG_VIEW_SCHEDULE = "TAG_DIALOG_VIEW_SCHEDULE"
        const val ARGS_HORSE = "ARGS_HORSE"
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCalendarBinding.bind(view)

        val horse = requireArguments().getParcelable<Horse>(ARGS_HORSE)

//        if(horseId != 0){
////            Toast.makeText(requireContext(),"from horse detail",Toast.LENGTH_SHORT).show()
//            binding.btnAddEvent.visibility = View.GONE
//            binding.bgBtnEvent.visibility = View.GONE
//        }



        lifecycleScope.launchWhenCreated {
            userPreferencesFlow.collect { up ->
                eventsAdapter = ScheduleListAdapter(listOf(), object: ScheduleListAdapter.EventScheduleListener {
                    override fun onView(position: Int, event: Event) {
                        ScheduleDialogFragment.newInstance(event,up.userId,up.stableId,horse).show(childFragmentManager,
                            TAG_DIALOG_VIEW_SCHEDULE)
                    }
                })
            }
        }



        binding.rvSchedule.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = eventsAdapter
        }

        eventsAdapter.notifyDataSetChanged()

        lifecycleScope.launchWhenCreated {

            binding.calendar.monthScrollListener = { month ->
                val title = "${monthTitleFormatter.format(month.yearMonth)} ${month.yearMonth.year}"

                binding.tvMonthTitle.text = title
                hideFilterChips(binding)
                lifecycleScope.launchWhenCreated {
                    userPreferencesFlow.collect {
                        eventsAdapter.eventList = listOf()
                        eventsAdapter.notifyDataSetChanged()

                        calendarViewModel.fetchEvents(
                            it.stableId,
                            monthFormatter.format(month.yearMonth),
                            month.yearMonth.year.toString(),
                            horse?.id ?: 0,
                        )

                        calendarViewModel.getHorses(SortOrder.BY_DATE,it.stableId)

                        binding.btnAddEvent.setOnClickListener { btn ->
                            AddEventDialogFragment.newInstance(it.userId,it.stableId,null,horse
                            ).show(childFragmentManager, TAG_DIALOG_ADD_EVENT)
                        }


                    }
                }

                selectedDate?.let {
                    selectedDate = null
                    binding.calendar.notifyDateChanged(it)
//                    updateAdapterForDate(it)
                }
            }


            binding.tvPrevMonth.setOnClickListener {
                binding.calendar.findFirstVisibleMonth()?.let {
                    binding.calendar.smoothScrollToMonth(it.yearMonth.previous)
                }
            }

            binding.tvNextMonth.setOnClickListener {
                binding.calendar.findFirstVisibleMonth()?.let {
                    binding.calendar.smoothScrollToMonth(it.yearMonth.next)
                }
            }

            val currentMonth = YearMonth.now()
            val daysOfWeek = daysOfWeekFromLocale()

            val dm = DisplayMetrics()
            val wm = requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.defaultDisplay.getMetrics(dm)

            binding.calendar.apply {
                val monthWidth = dm.widthPixels
                val dayWidth = monthWidth / 7
                val dayHeight = (dayWidth * 1.3).toInt()
                daySize = Size(dayWidth,dayHeight)
            }


            binding.calendar.setup(currentMonth.minusMonths(10),currentMonth.plusMonths(10),
                daysOfWeekFromLocale().first())
            binding.calendar.scrollToMonth(currentMonth)

            binding.chipGroupFilter.setOnCheckedChangeListener { group, checkedId ->
                    selectedDate.let {
                        when(checkedId){
                            R.id.chip_final_cut_off -> updateAdapterForDate(selectedDate,EventType.FINAL_CUT_OFF)
                            R.id.chip_interm_cut_off -> updateAdapterForDate(selectedDate,EventType.INTERM_CUT_OFF)
                            R.id.chip_race_sched -> updateAdapterForDate(selectedDate,EventType.RACE_SCHED)
                            R.id.chip_farrier -> updateAdapterForDate(selectedDate,EventType.FARRIER)
                            R.id.chip_stables_retire -> updateAdapterForDate(selectedDate,EventType.STABLES_RETIRE)
                            R.id.chip_stables_return -> updateAdapterForDate(selectedDate,EventType.STABLES_RETURN)
                            R.id.chip_stables_related -> updateAdapterForDate(selectedDate,EventType.STABLE_RELATED)
                            R.id.chip_business_trip -> updateAdapterForDate(selectedDate,EventType.BUSINESS_TRIP)
                            R.id.chip_others -> updateAdapterForDate(selectedDate,EventType.OTHERS)
                            else -> updateAdapterForDate(selectedDate)
                        }
                    }
            }

            class MonthViewContainer(view: View) : ViewContainer(view) {
                val legendLayout = CalendarHeaderBinding.bind(view).legendLayout.root
            }

            lifecycleScope.launchWhenCreated {
                userPreferencesFlow.collect { userPreferences ->


                    class DayViewContainer @Inject constructor(view: View, val userId: Int, val stableId: Int) : ViewContainer(view) {
                        lateinit var day: CalendarDay // Will be set when this container is bound.
                        val binding = CalendarDayBinding.bind(view)
                        init {
                            view.setOnClickListener {
                                if (day.owner == DayOwner.THIS_MONTH) {
                                    if (selectedDate != day.date) {
                                        val oldDate = selectedDate
                                        selectedDate = day.date
                                        binding.calendar.notifyDateChanged(day.date)
                                        oldDate?.let { binding.calendar.notifyDateChanged(it) }
                                        updateAdapterForDate(day.date)
                                        hideFilterChips(binding)
                                        val filters  = getAllFiltersForEventType(eventsAdapter.eventList)
                                        binding.filterBackground.visibility = if(filters.isEmpty()) View.GONE else View.VISIBLE
                                        binding.labelFilter.visibility = if(filters.isEmpty()) View.GONE else View.VISIBLE
                                        for(filter: EventType in filters){
                                            when(filter){
                                                EventType.FINAL_CUT_OFF -> {
                                                    binding.chipFinalCutOff.visibility = View.VISIBLE
                                                }
                                                EventType.INTERM_CUT_OFF -> {
                                                    binding.chipIntermCutOff.visibility = View.VISIBLE
                                                }
                                                EventType.RACE_SCHED -> {
                                                    binding.chipRaceSched.visibility = View.VISIBLE
                                                }
                                                EventType.FARRIER -> {
                                                    binding.chipFarrier.visibility = View.VISIBLE
                                                }
                                                EventType.STABLES_RETIRE -> {
                                                    binding.chipStablesRetire.visibility = View.VISIBLE
                                                }
                                                EventType.STABLES_RETURN -> {
                                                    binding.chipStablesReturn.visibility = View.VISIBLE
                                                }
                                                EventType.STABLE_RELATED -> {
                                                    binding.chipStablesRelated.visibility = View.VISIBLE
                                                }
                                                EventType.BUSINESS_TRIP -> {
                                                    binding.chipBusinessTrip.visibility = View.VISIBLE
                                                }
                                                EventType.OTHERS -> {
                                                    binding.chipOthers.visibility = View.VISIBLE
                                                }
                                            }
                                        }
                                        binding.bgBtnEvent.visibility = View.VISIBLE



                                    }
                                }
                            }
                        }
                    }


                    binding.calendar.dayBinder = object: DayBinder<DayViewContainer> {
                        override fun bind(container: DayViewContainer, day: CalendarDay) {
                            container.day = day
                            val textView = container.binding.exFiveDayText
                            val layout = container.binding.exFiveDayLayout
                            textView.text = day.date.dayOfMonth.toString()

                            Timber.v(day.toString())
                            container.binding.viewContainer.removeAllViews()


                            if (day.owner == DayOwner.THIS_MONTH) {
                                textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorBlackText))
                                layout.setBackgroundResource(if (selectedDate == day.date) R.drawable.state_calendar_selected else 0)


//                        Toast.makeText(requireContext(),day.date.toString(),Toast.LENGTH_SHORT).show()

                                val d = day.date
                                val ev = events.filter { e ->
                                    val startD = LocalDate.parse(e.date)
                                    if(e.dateEnd != null){
                                        val endD = LocalDate.parse(e.dateEnd)
                                        d.checkIfWithinRange(startD,endD)
                                    }else{
                                        e.date == day.date.toString()
                                    }
                                }

                                if(events != null){
                                    var extras = 0
                                        for((x, event: Event) in ev.withIndex()){
                                            if(x < 4) {
                                                val title = if(day.date.toString() == event.date || day.date.toString().substring(8,10) == "01") event.title else ""
                                                container.binding.viewContainer.addView(createRectTextView(title,event,horse))
                                            }else{
                                                extras++
                                            }
                                        }
                                    if(extras > 0){
                                        container.binding.viewContainer.addView(createRectTextView("+${extras}",null,null))
                                    }
                                }
                            } else {
                                textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorPrimaryDarkAlpha50))
                                layout.background = null
                            }
                        }

                        override fun create(view: View): DayViewContainer = DayViewContainer(view,userPreferences.userId,userPreferences.stableId)

                    }
                }

            }



            binding.calendar.monthHeaderBinder = object: MonthHeaderFooterBinder<MonthViewContainer> {
                override fun bind(container: MonthViewContainer, month: CalendarMonth) {
//                    if (container.legendLayout.tag == null) {
//                        container.legendLayout.tag = month.yearMonth
//                        container.legendLayout.children.map { it as TextView }.forEachIndexed { index, tv ->
//                            tv.text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
//                                .toUpperCase(Locale.ENGLISH)
//                            tv.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorBlackText))
//                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
//                        }
//                        month.yearMonth
//                    }
                }

                override fun create(view: View): MonthViewContainer = MonthViewContainer(view)

            }

            lifecycleScope.launchWhenStarted {
                calendarViewModel.addEventFlow.collect { event ->
                    when(event){
                        is CalendarViewModel.AddEvent.Error -> {
                            Toast.makeText(requireContext(),event.errorMessage, Toast.LENGTH_SHORT).show()
                        }
                        is CalendarViewModel.AddEvent.Loading -> {
                            if(event.isLoading){
                            }
                        }
                        is CalendarViewModel.AddEvent.Success -> {
                            Toast.makeText(requireContext(),event.message, Toast.LENGTH_SHORT).show()
//                            calendarBinding?.calendar?.scrollToMonth(YearMonth.now())
                            selectedDate?.let {
                                selectedDate = null
                                calendarBinding?.calendar?.notifyDateChanged(it)
                                updateAdapterForDate(it,null)
                            }

                        }
                    }
                }

            }

            calendarViewModel.events.collect{
                when(it){
                    is DataState.Data -> {
//                        eventsAdapter.eventList = it.data
//                        eventsAdapter.notifyDataSetChanged()
//                        events = listOf()
                        events = it.data
                        binding.calendar.notifyCalendarChanged()
                    }
                    DataState.Empty -> {

                    }
                    is DataState.Error -> {
                        //TODO:
                        Timber.v(it.exception)
                    }
                    is DataState.Loading -> {
                        //TODO:
                    }
                }
            }
        }


    }



    private fun createRectTextView(textContent: String, event: Event?, horse: Horse?): TextView{
        val eventType = event?.eventType?.parseEventType()
        val textView = TextView(ContextThemeWrapper(requireContext(),R.style.InbloCalendarEventText)).apply {
            background =  ContextCompat.getDrawable(requireContext(),R.drawable.shape_rounded_corners_3dp)
            if(eventType != null){
                setBackgroundColor(ContextCompat.getColor(requireContext(), eventType.colorDarker))
            }else{
                setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorBlackText))
            }
            text = textContent
        }
        if(event != null){
            lifecycleScope.launchWhenCreated {
                userPreferencesFlow.collect { up ->
                    textView.setOnClickListener {
                        ScheduleDialogFragment.newInstance(event,up.userId,up.stableId,horse).show(
                            childFragmentManager,
                            TAG_DIALOG_VIEW_SCHEDULE
                        )
                    }
                }
            }
        }
        return textView
    }

//    private fun createRectTextView(textContent: String, eventType: EventType): TextView{
//        return TextView(requireContext()).apply {
//            val params = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            setPadding(3,2,3,2)
//            params.setMargins(0,0,0,5)
//            maxLines = 1
//            ellipsize = TextUtils.TruncateAt.END
//            val typeFace = ResourcesCompat.getFont(requireContext(),R.font.roboto_bold)
//            setTypeface(typeface)
//            layoutParams = params
////            background = ContextCompat.getDrawable(requireContext(),R.drawable.shape_rounded_corners_3dp)
//            setBackgroundColor(ContextCompat.getColor(requireContext(), eventType.colorDarker))
////            backgroundTintList = ContextCompat.getColorStateList(requireContext(), eventType.colorDarker)
//            text = textContent
//            textSize = 6f
//            setTextColor(ContextCompat.getColor(requireContext(),android.R.color.white))
//            gravity = Gravity.CENTER
//        }
//    }

    private fun LocalDate.checkIfWithinRange(startDate: LocalDate, endDate: LocalDate): Boolean{
        return (this.isAfter(startDate)) && (this.isBefore(endDate)) || (this == startDate || this == endDate)
    }

    private fun updateAdapterForDate(date: LocalDate?, eventTypeFilter: EventType? = null) {
        if(date != null) {
            eventsAdapter.eventList = events.filter {
                val startD = LocalDate.parse(it.date)
                var e = if(it.dateEnd != null) {
                    val endD = LocalDate.parse(it.dateEnd)
                    date.checkIfWithinRange(startD,endD)
                }else{
                    it.date == date.toString()
                }
                var v: Boolean
                if (eventTypeFilter != null) {
                    v = eventTypeFilter.type == it.eventType && e
                }else {
                    v = e
                }
                v
            }


            eventsAdapter.notifyDataSetChanged()
        }
    }

    private fun hideFilterChips(binding: FragmentCalendarBinding){
        binding.chipFinalCutOff.visibility = View.GONE
        binding.chipIntermCutOff.visibility = View.GONE
        binding.chipRaceSched.visibility = View.GONE
        binding.chipFarrier.visibility = View.GONE
        binding.chipStablesRetire.visibility = View.GONE
        binding.chipStablesReturn.visibility = View.GONE
        binding.chipStablesRelated.visibility = View.GONE
        binding.chipBusinessTrip.visibility = View.GONE
        binding.chipOthers.visibility = View.GONE
    }

    private fun getAllFiltersForEventType(events: List<Event>): List<EventType> {
        var eventTypes = mutableSetOf<EventType>()
        for(event: Event in events){
            eventTypes.add(event.eventType.parseEventType())
        }
        return eventTypes.toList()
    }


    override fun onDestroyView() {
        calendarBinding = null
        super.onDestroyView()
    }
}