package com.colinjp.inblo.android.presentation.ui.horse_detail.tabs

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.databinding.FragmentTabHorseTrainingBinding
import com.colinjp.inblo.android.presentation.ui.horse_detail.HorseDetailFragment
import com.colinjp.inblo.android.presentation.ui.horse_detail.HorseDetailViewModel
import com.colinjp.inblo.android.presentation.ui.horse_detail.dialogs.RecordTrainingDialogFragment
import com.colinjp.inblo.domain.model.HorseTrainingRecord
import com.colinjp.inblo.util.DataState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class HorseTrainingTabFragment: Fragment(R.layout.fragment_tab_horse_training) {

    private val TAG_RECORD_TRAINING_DIALOG = "RECORD_TRAINING_DIALOG"
    private var horseTrainingBinding: FragmentTabHorseTrainingBinding? = null
    private val horseDetailViewModel by activityViewModels<HorseDetailViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTabHorseTrainingBinding.bind(view)
        horseTrainingBinding = binding
        val horse = (requireParentFragment().requireParentFragment() as HorseDetailFragment).args.horse


        binding.btnRecordToday.setOnClickListener {
            val dialog = RecordTrainingDialogFragment.newInstance(horse.id.toString())
            dialog.show(childFragmentManager,TAG_RECORD_TRAINING_DIALOG)
        }


        binding.horScrollRecycler.viewTreeObserver.addOnScrollChangedListener {
            val scrollX = binding.horScrollRecycler.scrollX
            binding.horScrollLabels.post {
                binding.horScrollLabels.scrollTo(scrollX,0)
            }
        }



        val adapter = HorseTrainingRecordsAdapter(
            listOf(), object: HorseTrainingRecordsAdapter.HorseTrainingRecordListener{
                override fun onEdit(position: Int, horseTrainingRecord: HorseTrainingRecord) {
                    val dialog = RecordTrainingDialogFragment.newInstance(horse.id.toString(),horseTrainingRecord)
                    dialog.show(childFragmentManager,TAG_RECORD_TRAINING_DIALOG)
                }

                override fun onDelete(position: Int, horseTrainingRecord: HorseTrainingRecord) {
                    MaterialAlertDialogBuilder(requireContext(),R.style.ThemeOverlay_App_MaterialAlertDialog)
                        .setNeutralButton("Cancel",null)
                        .setNegativeButton("DELETE") { dialog, which ->
                            if (which == DialogInterface.BUTTON_NEGATIVE) {
                                horseDetailViewModel.removeHorseTraining(horseTrainingRecord.id.toString())
                            }
                        }
                        .setTitle("Warning!")
                        .setMessage("Are you sure you want to delete this item?")
                        .setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_priority_high_24)).show()
                }

            }
        )
        binding.rvHorseTrainings.adapter = adapter
        binding.rvHorseTrainings.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.VERTICAL,false)

        lifecycleScope.launchWhenCreated {
            horseDetailViewModel.horseTrainingRecords.collect {
                when(it){
                    is DataState.Data -> {
                        adapter.horseTrainingRecordList = it.data
                        Timber.v(it.toString())
                        if(it.data.isNotEmpty()){
                            binding.flowColumns.visibility = View.VISIBLE
                        }else{
                            binding.flowColumns.visibility = View.INVISIBLE
                        }
                        adapter.notifyDataSetChanged()
                    }
                    DataState.Empty -> { }
                    is DataState.Error -> {
                        Timber.v(it.toString())

                    }
                    is DataState.Loading -> {
                        Timber.v(it.toString())

                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        horseTrainingBinding = null
        super.onDestroyView()
    }
}

