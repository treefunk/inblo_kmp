package com.colinjp.inblo.android.presentation.ui.calendar.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.databinding.DialogAddEventBinding
import com.colinjp.inblo.android.domain.model.EventType
import com.colinjp.inblo.android.domain.util.initChoices
import com.colinjp.inblo.android.domain.util.initDatePickerSpinner
import com.colinjp.inblo.android.presentation.ui.BaseDialogFragment
import com.colinjp.inblo.android.presentation.ui.calendar.CalendarViewModel
import com.colinjp.inblo.domain.model.Event
import com.colinjp.inblo.domain.model.Horse
import com.colinjp.inblo.util.DataState
import com.google.android.material.textfield.TextInputLayout.END_ICON_NONE
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import kotlinx.coroutines.flow.collect
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import timber.log.Timber
import java.util.*

class AddEventDialogFragment: BaseDialogFragment() {

    private val calendarViewmodel by activityViewModels<CalendarViewModel>()
    private var _binding: DialogAddEventBinding? = null

    companion object {

        const val ARGS_STABLE_ID = "ARG_STABLE_ID"
        const val ARGS_USER_ID = "ARG_USER_ID"
        const val ARGS_DATE = "ARG_DATE"
        const val ARGS_EVENT = "ARG_EVENT"
        const val ARGS_HORSE = "ARG_HORSE"
        const val TAG_DIALOG_TIME_PICKER = "TAG_TIME_PICKER"

        fun newInstance(userId: Int, stableId: Int, event: Event? = null, horse: Horse? = null): AddEventDialogFragment {
            val args = Bundle()
            args.putInt(ARGS_STABLE_ID,stableId)
//            args.putString(ARGS_DATE,date)
            args.putInt(ARGS_USER_ID,userId)
            args.putParcelable(ARGS_EVENT, event)
            args.putParcelable(ARGS_HORSE,horse)
            val fragment = AddEventDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
//        val view = inflater.inflate(R.layout.dialog_record_daily.xml,container,false)
        val binding = DialogAddEventBinding.inflate(inflater,container,false)
        _binding = binding

        val stableId = requireArguments().getInt(ARGS_STABLE_ID)
        val userId = requireArguments().getInt(ARGS_USER_ID)
        val event = requireArguments().getParcelable<Event>(ARGS_EVENT)
        val horse = requireArguments().getParcelable<Horse>(ARGS_HORSE)

        if(horse != null){
            binding.acHorseName.setText(horse.name)
            binding.acHorseName.setTag(horse.id.toString())
            binding.acHorseName.isEnabled = false
            binding.tilHorseName.endIconMode = END_ICON_NONE
        }

        if(event != null){
            binding.etEventTitle.setText(event.title)
            binding.acEventType.setText(event.eventType,false)
            binding.etDateStart.setText(event.date)
            binding.etDateEnd.setText(event.dateEnd)
            if(binding.etDateEnd.text.toString().isNotBlank()){
                binding.ibClearDateEnd.visibility = View.VISIBLE
            }else{
                binding.ibClearDateEnd.visibility = View.GONE
            }
            binding.etStart.setText(event.start)
            binding.etEnd.setText(event.end)
            binding.etNotes.setText(event.notes)
        }

//        val date = requireArguments().getString(ARGS_DATE)!!

        binding.etDateStart.initDatePickerSpinner(requireContext(),false){
            binding.etDateEnd.setText("")
        }
        binding.etDateEnd.initDatePickerSpinner(requireContext(),false){
            if(binding.etDateStart.text.toString().isNotBlank()){
                val start = LocalDate.parse(binding.etDateStart.text.toString())
                val end = LocalDate.parse(binding.etDateEnd.text.toString())

                if(end < start){
                    binding.etDateStart.setText("")
                }
            }
        }
        binding.ibClearDateEnd.setOnClickListener {
            binding.etDateEnd.setText("")
        }

        if(event != null){
//            binding.acWeather.setText(event.weather)
//            binding.acAccidentSite.setText(event.accidentSite)
//            binding.acAccidentPart.setText(event.accidentPart)
//            binding.acAccidentType.setText(event.accidentType)
//            binding.etDate.setText(event.date)
//            binding.etBodyTemp.setText(event.bodyTemperature.toString())
//            binding.etWeight.setText(event.horseWeight.toString())
//            binding.etNotes.setText(event.memo)
        }

        lifecycleScope.launchWhenCreated {
            calendarViewmodel.horses.collect { horses ->
                when(horses){
                    is DataState.Data -> {
                        binding.acHorseName.initChoices(
                            requireContext(),
                            horses.data.map { it.name.toString() }, { _,pos ->
                                if(pos != 0){
                                    binding.acHorseName.tag = horses.data[pos - 1].id.toString()
                                }else{
                                    binding.acHorseName.tag = ""
                                }
                            }
                        )
                        Timber.v("horses -> $horses")
                    }
                    DataState.Empty -> { }
                    is DataState.Error -> {
                        //TODO:
                        Timber.e(horses.exception)
                    }
                    is DataState.Loading -> {
                        //TODO:
                    }
                }
            }
        }

//        binding.labelDate.text = date



        binding.etStart.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val timePicker = MaterialTimePicker.Builder()
                .setTitleText(binding.etStart.hint)
                .setHour(hour)
                .setTimeFormat(CLOCK_24H)
                .setMinute(minute)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val h = if(timePicker.hour.toString().length == 1) "0" + timePicker.hour else timePicker.hour
                val m = if(timePicker.minute.toString().length == 1) "0" + timePicker.minute else timePicker.minute
                binding.etStart.setText("${h}:${m}")
            }
            timePicker.show(childFragmentManager, TAG_DIALOG_TIME_PICKER)
        }

        binding.etEnd.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val timePicker = MaterialTimePicker.Builder()
                .setTitleText(binding.etEnd.hint)
                .setHour(hour)
                .setTimeFormat(CLOCK_24H)
                .setMinute(minute)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val h = if(timePicker.hour.toString().length == 1) "0" + timePicker.hour else timePicker.hour
                val m = if(timePicker.minute.toString().length == 1) "0" + timePicker.minute else timePicker.minute
                binding.etEnd.setText("${h}:${m}")
            }
            timePicker.show(childFragmentManager, TAG_DIALOG_TIME_PICKER)
        }

        val eventTypes = EventType.values().map { it.type }

        binding.acEventType.initChoices(
            requireContext(),eventTypes
        )

        binding.etDateEnd.addTextChangedListener {
            if(binding.etDateEnd.text.toString().isNotBlank()){
                binding.ibClearDateEnd.visibility = View.VISIBLE
            }else{
                binding.ibClearDateEnd.visibility = View.GONE
            }
        }




        binding.ibCloseDialog.setOnClickListener { this@AddEventDialogFragment.dismiss() }
        binding.btnRecordCondition.setOnClickListener {

            val horseId = if(binding.acHorseName.tag != "") binding.acHorseName.tag.toString().toInt() else null
            val title = binding.etEventTitle.text.toString()
            val type = binding.acEventType.text.toString()
            val start = binding.etStart.text.toString()
            val end = binding.etEnd.text.toString().let { if(it.isNotBlank()) it else start }
            val notes = binding.etNotes.text.toString()
            val dateStart = binding.etDateStart.text.toString()
            val dateEndString = binding.etDateEnd.text.toString()
            val dateEnd = if(dateEndString.isBlank() || dateEndString == dateStart) null else dateEndString



            if( type.isBlank() || dateStart.isBlank()){
                Toast.makeText(requireContext(),"すべての項目を入力してください。", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            calendarViewmodel.addCalendarEvent(
                horseId,
                userId,
                stableId,
                dateStart,
                dateEnd,
                title,
                type,
                start,
                end,
                notes,
                event?.id
            )
            this.dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}


