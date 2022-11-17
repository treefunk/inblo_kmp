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
import com.colinjp.inblo.android.databinding.FragmentTabHorseDailyBinding
import com.colinjp.inblo.android.presentation.ui.horse_detail.HorseDetailFragment
import com.colinjp.inblo.android.presentation.ui.horse_detail.HorseDetailViewModel
import com.colinjp.inblo.android.presentation.ui.horse_detail.dialogs.ConditionGraphDialogFragment
import com.colinjp.inblo.android.presentation.ui.horse_detail.dialogs.RecordDailyDialogFragment
import com.colinjp.inblo.android.presentation.ui.horse_detail.dialogs.ViewAttachedFileDialogFragment
import com.colinjp.inblo.domain.model.DailyAttachedFile
import com.colinjp.inblo.domain.model.HorseDaily
import com.colinjp.inblo.util.DataState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class HorseDailyTabFragment: Fragment(R.layout.fragment_tab_horse_daily) {

    private val TAG_ADD_HORSE_CONDITION = "ADD_HORSE_CONDITION"
    private val TAG_VIEW_GRAPH_CONDITION = "VIEW_GRAPH_CONDITION"

    private var horseConditionBinding: FragmentTabHorseDailyBinding? = null
    private val horseDetailViewModel by activityViewModels<HorseDetailViewModel>()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTabHorseDailyBinding.bind(view)
        horseConditionBinding = binding

        val horse = (requireParentFragment().requireParentFragment() as HorseDetailFragment).args.horse


        binding.btnRecordToday.setOnClickListener {
            val dialog = RecordDailyDialogFragment.newInstance(horse.id.toString())
            dialog.show(childFragmentManager,TAG_ADD_HORSE_CONDITION)
        }

        binding.btnTemperatureAndWeightGraph.setOnClickListener {
            val dialog = ConditionGraphDialogFragment.newInstance()
            dialog.show(childFragmentManager,TAG_VIEW_GRAPH_CONDITION)
        }

        binding.horScrollRecycler.viewTreeObserver.addOnScrollChangedListener {
            val scrollX = binding.horScrollRecycler.scrollX
            binding.horScrollLabels.post {
                binding.horScrollLabels.scrollTo(scrollX,0)
            }
        }

        val adapter = HorseDailyAdapter(
            listOf(), object: HorseDailyAdapter.HorseDailyListener{
                override fun onEdit(position: Int, horseDaily: HorseDaily) {
                    val dialog = RecordDailyDialogFragment.newInstance(horse.id.toString(),horseDaily)
                    dialog.show(childFragmentManager,TAG_ADD_HORSE_CONDITION)
                }

                override fun onDelete(position: Int, horseDaily: HorseDaily) {
                    MaterialAlertDialogBuilder(requireContext(),R.style.ThemeOverlay_App_MaterialAlertDialog)
                        .setNeutralButton("Cancel",null)
                        .setNegativeButton("DELETE") { dialog, which ->
                            if (which == DialogInterface.BUTTON_NEGATIVE) {
                                horseDetailViewModel.removeHorseDaily(horseDaily.id.toString())
                            }
                        }
                        .setTitle("Warning!")
                        .setMessage("Are you sure you want to delete this item?")
                        .setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_priority_high_24)).show()
                }

                override fun onViewFiles(horseDaily: HorseDaily) {
                    ViewAttachedFileDialogFragment.newInstance(
                        ArrayList(horseDaily.attachedFiles?.map { DailyAttachedFile.createFromDto(it) }),ViewAttachedFileDialogFragment.DIALOGTYPE.DAILY.dirString
                    ).show(childFragmentManager,"VIEW_ATTACH_FILES")
                }
            }
        )
        binding.rvHorseConditions.adapter = adapter
        binding.rvHorseConditions.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)



        lifecycleScope.launchWhenCreated {
            horseDetailViewModel.horseDailyReports.collect {
                when (it) {
                    is DataState.Data -> {
                        adapter.horseDailyList = it.data.data.map {
                            HorseDaily.createFromDto(it)
                        }
                        adapter.hiddenColumns = it.data.hiddenColumns
                        Timber.v("hidden columns ${it.data.hiddenColumns}")





                        Timber.v(it.toString())
                        if (it.data.data.isNotEmpty()) {
                            binding.flowColumns.visibility = View.VISIBLE
                            binding.btnTemperatureAndWeightGraph.visibility = View.VISIBLE
                        }


                        binding.labelDate.visibility = if(it.data.hiddenColumns.contains("date")) View.GONE else View.VISIBLE
                        binding.labelTemperature.visibility = if(it.data.hiddenColumns.contains("body_temperature")) View.GONE else View.VISIBLE
                        binding.labelWeight.visibility = if(it.data.hiddenColumns.contains("horse_weight")) View.GONE else View.VISIBLE
                        binding.labelConditionGroup.visibility = if(it.data.hiddenColumns.contains("condition_group")) View.GONE else View.VISIBLE
                        binding.labelRiderName.visibility = if(it.data.hiddenColumns.contains("rider")) View.GONE else View.VISIBLE
                        binding.labelTrainingType.visibility = if(it.data.hiddenColumns.contains("training_type")) View.GONE else View.VISIBLE
                        binding.labelTrainingAmount.visibility = if(it.data.hiddenColumns.contains("training_amount")) View.GONE else View.VISIBLE
                        binding.label5F.visibility = if(it.data.hiddenColumns.contains("5f_time")) View.GONE else View.VISIBLE
                        binding.label4F.visibility = if(it.data.hiddenColumns.contains("4f_time")) View.GONE else View.VISIBLE
                        binding.label3F.visibility = if(it.data.hiddenColumns.contains("3f_time")) View.GONE else View.VISIBLE
                        binding.labelNote.visibility = if(it.data.hiddenColumns.contains("memo")) View.GONE else View.VISIBLE

                        if(it.data.data.isEmpty()) {
                            binding.flowColumns.visibility = View.INVISIBLE
                            binding.btnTemperatureAndWeightGraph.visibility = View.INVISIBLE
                        }
                        adapter.notifyDataSetChanged()
                    }
                    DataState.Empty -> {}
                    is DataState.Error -> {
                        Timber.v(it.toString())
                    }
                    is DataState.Loading -> {
                        Timber.v(it.toString())

                    }
                }
            }
        }
//        lifecycleScope.launchWhenCreated {
//            horseDetailViewModel.horseDailyReports.collect {
//                when(it){
//                    is DataState.Data -> {
//                        adapter.horseDailyList = it.data
//                        Timber.v(it.toString())
//                        if(it.data.isNotEmpty()){
//                            binding.flowColumns.visibility = View.VISIBLE
//                            binding.btnTemperatureAndWeightGraph.visibility = View.VISIBLE
//                        }else{
//                            binding.flowColumns.visibility = View.INVISIBLE
//                            binding.btnTemperatureAndWeightGraph.visibility = View.INVISIBLE
//                        }
//                        adapter.notifyDataSetChanged()
//                    }
//                    DataState.Empty -> { }
//                    is DataState.Error -> {
//                        Timber.v(it.toString())
//
//                    }
//                    is DataState.Loading -> {
//
//                        Timber.v(it.toString())
//                    }
//                }
//            }
//        }


    }

    override fun onDestroyView() {
        horseConditionBinding = null
        super.onDestroyView()
    }


}