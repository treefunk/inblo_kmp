package com.colinjp.inblo.android.presentation.ui.horse_detail.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.colinjp.inblo.android.databinding.DialogRecordTrainingBinding
import com.colinjp.inblo.android.domain.util.initChoices
import com.colinjp.inblo.android.domain.util.initDatePickerSpinner
import com.colinjp.inblo.android.presentation.ui.BaseDialogFragment
import com.colinjp.inblo.android.presentation.ui.horse_detail.HorseDetailViewModel
import com.colinjp.inblo.domain.model.HorseTrainingRecord


class RecordTrainingDialogFragment: BaseDialogFragment() {

    private val horseDetailViewModel by activityViewModels<HorseDetailViewModel>()
    private var _binding: DialogRecordTrainingBinding? = null

    companion object {

        private const val ARG_HORSE_ID = "ARG_HORSE_ID"
        private const val ARG_HORSE_TRAINING_RECORD = "ARG_HORSE_TRAINING_RECORD"

        fun newInstance(horseId: String, horseTrainingRecord: HorseTrainingRecord? = null): RecordTrainingDialogFragment {
            val args = Bundle()
            args.putString(ARG_HORSE_ID,horseId)
            args.putParcelable(ARG_HORSE_TRAINING_RECORD,horseTrainingRecord)
            val fragment = RecordTrainingDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = DialogRecordTrainingBinding.inflate(inflater,container,false)
        _binding = binding
//        val view = inflater.inflate(R.layout.dialog_record_training,container,false)
        val horseId = requireArguments().getString(RecordDailyDialogFragment.ARG_HORSE_ID)!!
        val horseTraining = requireArguments().getParcelable<HorseTrainingRecord>(RecordTrainingDialogFragment.ARG_HORSE_TRAINING_RECORD)

        if(horseTraining != null){
            binding.etDate.setText(horseTraining.date)
            binding.acWeather.setText(horseTraining.weather)
            binding.acTrainingType.setText(horseTraining.trainingType)
            binding.acTrainingContent.setText(horseTraining.trainingDetail)
            binding.et6f.setText(horseTraining.time6F.toString())
            binding.et5f.setText(horseTraining.time5F.toString())
            binding.et4f.setText(horseTraining.time4F.toString())
            binding.et3f.setText(horseTraining.time3F.toString())
            binding.et2f.setText(horseTraining.time2F.toString())
            binding.et1f.setText(horseTraining.time1F.toString())
            binding.etNotes.setText(horseTraining.memo)
        }

        binding.etDate.initDatePickerSpinner(requireContext())
        binding.ibCloseDialog.setOnClickListener { this@RecordTrainingDialogFragment.dismiss() }
        binding.acWeather.initChoices(requireContext(), listOf("晴","曇","雨","その他"))
        binding.acTrainingType.initChoices(requireContext(), listOf("角馬場","内馬場","コース追い","中間追切","最終追切","その他"))
        binding.acTrainingContent.initChoices(requireContext(), listOf("乗り運動","軽め","800m","1000m","1200m"), { _,_ -> binding.et6f.requestFocus() }
        )

        binding.btnRecordCondition.setOnClickListener {
            val date = binding.etDate.text.toString()
            val weather = binding.acWeather.text.toString()
            val trainingType = binding.acTrainingType.text.toString()
            val trainingDetail = binding.acTrainingContent.text.toString()
            val time6F = binding.et6f.text.toString()
            val time5F = binding.et5f.text.toString()
            val time4F = binding.et4f.text.toString()
            val time3F = binding.et3f.text.toString()
            val time2F = binding.et2f.text.toString()
            val time1F = binding.et1f.text.toString()
            val notes = binding.etNotes.text.toString()

            if(date.isBlank()){
                Toast.makeText(requireContext(),"日付を入力してください。", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(trainingType.isBlank()){
                Toast.makeText(requireContext(),"調教タイプを選択してください。", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(trainingDetail.isBlank()){
                Toast.makeText(requireContext(),"調教内容を選択してください。", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            horseDetailViewModel.addHorseTraining(
                horseId,
                date,
                weather,
                trainingType,
                trainingDetail,
                notes,
                time6F,
                time5F,
                time4F,
                time3F,
                time2F,
                time1F,
                horseTraining?.id?.toString()
            )

            this.dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}