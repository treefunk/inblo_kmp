package com.colinjp.inblo.android.presentation.ui.calendar.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.databinding.DialogScheduleBinding
import com.colinjp.inblo.android.domain.util.parseEventType
import com.colinjp.inblo.android.presentation.ui.BaseDialogFragment
import com.colinjp.inblo.android.presentation.ui.calendar.CalendarViewModel
import com.colinjp.inblo.domain.model.Event
import com.colinjp.inblo.domain.model.Horse
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ScheduleDialogFragment: BaseDialogFragment() {

    private var _binding: DialogScheduleBinding? = null
    private val calendarViewmodel by activityViewModels<CalendarViewModel>()

    companion object {

        const val ARGS_EVENT = "ARG_EVENT"
        const val ARGS_USER_ID = "ARG_USER_ID"
        const val ARGS_STABLE_ID = "ARG_STABLE_ID"
        const val ARGS_HORSE = "ARG_HORSE"


        fun newInstance(event: Event,userId: Int, stableId: Int, horse: Horse? = null): ScheduleDialogFragment {
            val args = Bundle()
            args.putInt(ARGS_USER_ID,userId)
            args.putInt(ARGS_STABLE_ID,stableId)
            args.putParcelable(ARGS_HORSE,horse)
            args.putParcelable(ARGS_EVENT, event)
            val fragment = ScheduleDialogFragment()
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
        val binding = DialogScheduleBinding.inflate(inflater,container,false)
        _binding = binding

        val event = requireArguments().getParcelable<Event>(ARGS_EVENT)!!
        val horse = requireArguments().getParcelable<Horse>(ARGS_HORSE)
        val userId = requireArguments().getInt(ARGS_USER_ID)
        val stableId = requireArguments().getInt(ARGS_STABLE_ID)


        val eventType = event.eventType.parseEventType()

        binding.viewColor.setBackgroundColor(ContextCompat.getColor(requireContext(),eventType.colorDark))
        binding.btnClose.backgroundTintList = ContextCompat.getColorStateList(requireContext(),eventType.colorDarker)
        binding.tvTime.text = if(event.start.isNotBlank()) event.start else "N/A" + ":" + if(event.end.isNotBlank()) event.end else "N/A"
        binding.tvDate.text = event.date
        binding.tvScheduleName.text = event.title
        binding.tvScheduleDescription.text = event.notes
        binding.btnPersonInCharge.text = "担当者 : ${event.user?.username}"

        binding.btnEdit.setOnClickListener {
            this@ScheduleDialogFragment.dismiss()
            AddEventDialogFragment.newInstance(
                userId,
                stableId,
                event,
                horse
            ).show(parentFragmentManager,"edit_event_dialog")
        }

        binding.btnTrash.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setNeutralButton("Cancel",null)
                .setNegativeButton("DELETE") { dialog, which ->
                    if (which == DialogInterface.BUTTON_NEGATIVE) {
                        calendarViewmodel.removeEvent(event.id,event.date)
                        this@ScheduleDialogFragment.dismiss()
                    }
                }
                .setTitle("Warning!")
                .setMessage("Are you sure you want to delete this event?")
                .setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_priority_high_24)).show()
        }




        binding.btnClose.setOnClickListener { this@ScheduleDialogFragment.dismiss() }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}


