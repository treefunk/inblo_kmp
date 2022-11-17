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
import com.colinjp.inblo.android.databinding.FragmentTabHorseTreatmentBinding
import com.colinjp.inblo.android.presentation.ui.horse_detail.HorseDetailFragment
import com.colinjp.inblo.android.presentation.ui.horse_detail.HorseDetailViewModel
import com.colinjp.inblo.android.presentation.ui.horse_detail.dialogs.RecordTreatmentDialogFragment
import com.colinjp.inblo.android.presentation.ui.horse_detail.dialogs.ViewAttachedFileDialogFragment
import com.colinjp.inblo.domain.model.DailyAttachedFile
import com.colinjp.inblo.domain.model.HorseTreatment
import com.colinjp.inblo.util.DataState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class HorseTreatmentTabFragment: Fragment(R.layout.fragment_tab_horse_treatment) {

    private val TAG_RECORD_TREATMENT_DIALOG = "RECORD_TREATMENT_DIALOG"
    private var horseTreatmentBinding: FragmentTabHorseTreatmentBinding? = null
    private val horseDetailViewModel by activityViewModels<HorseDetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTabHorseTreatmentBinding.bind(view)
        horseTreatmentBinding = binding

        val horse = (requireParentFragment().requireParentFragment() as HorseDetailFragment).args.horse


        binding.btnRecordToday.setOnClickListener {
            val dialog = RecordTreatmentDialogFragment.newInstance(horse.id.toString())
            dialog.show(childFragmentManager,TAG_RECORD_TREATMENT_DIALOG)
        }

        binding.horScrollRecycler.viewTreeObserver.addOnScrollChangedListener {
            val scrollX = binding.horScrollRecycler.scrollX
            binding.horScrollLabels.post {
                binding.horScrollLabels.scrollTo(scrollX,0)
            }
        }

        val adapter = HorseTreatmentsAdapter(
            listOf(), object: HorseTreatmentsAdapter.HorseTreatmentListener{
                override fun onEdit(position: Int, horseTreatment: HorseTreatment) {
                    val dialog = RecordTreatmentDialogFragment.newInstance(horse.id.toString(),horseTreatment)
                    dialog.show(childFragmentManager,TAG_RECORD_TREATMENT_DIALOG)
                }

                override fun onDelete(position: Int, horseTreatment: HorseTreatment) {
                    MaterialAlertDialogBuilder(requireContext(),R.style.ThemeOverlay_App_MaterialAlertDialog)
                        .setNeutralButton("Cancel",null)
                        .setNegativeButton("DELETE") { dialog, which ->
                            if (which == DialogInterface.BUTTON_NEGATIVE) {
                                horseDetailViewModel.removeHorseTreatment(horseTreatment.id.toString())
                            }
                        }
                        .setTitle("Warning!")
                        .setMessage("Are you sure you want to delete this item?")
                        .setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_priority_high_24)).show()
                }

                override fun onViewFiles(horseTreatment: HorseTreatment) {
                    ViewAttachedFileDialogFragment.newInstance(
                        ArrayList(horseTreatment.attachedFiles?.map { DailyAttachedFile.createFromDto(it) }),ViewAttachedFileDialogFragment.DIALOGTYPE.TREATMENT.dirString
                    ).show(childFragmentManager,"VIEW_ATTACH_FILES")
                }

            }
        )
        binding.rvHorseTreatments.adapter = adapter
        binding.rvHorseTreatments.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.VERTICAL,false)

        lifecycleScope.launchWhenCreated {
            horseDetailViewModel.horseTreatments.collect {
                when(it){
                    is DataState.Data -> {
                        adapter.horseTreatmentList = it.data
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
        horseTreatmentBinding = null
        super.onDestroyView()
    }
}