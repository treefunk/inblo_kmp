package com.colinjp.inblo.android.presentation.ui.horse_list_archive

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.databinding.FragmentHorseArchiveListBinding
import com.colinjp.inblo.android.databinding.FragmentHorseListBinding
import com.colinjp.inblo.android.domain.util.UserPreferences
import com.colinjp.inblo.domain.model.Horse
import com.colinjp.inblo.util.DataState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class HorseArchiveListFragment: Fragment(R.layout.fragment_horse_archive_list) {


    private var fragmenthHorseArchiveListBinding: FragmentHorseArchiveListBinding? = null
    private val horseListViewModel by activityViewModels<HorseArchiveListViewModel>()
    @Inject
    lateinit var userPreferencesFlow: Flow<UserPreferences>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHorseArchiveListBinding.bind(view)
        fragmenthHorseArchiveListBinding = binding




        val adapter = HorseArchiveListAdapter(
            listOf(), object: HorseArchiveListAdapter.HorseArchiveListListener {
                override fun onRestore(position: Int, horse: Horse) {
                    MaterialAlertDialogBuilder(requireContext(),R.style.ThemeOverlay_App_MaterialAlertDialog)
                        .setNeutralButton("Cancel",null)
                        .setPositiveButton("RESTORE") { dialog, which ->
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                horseListViewModel.restoreHorse(horse.id.toString())
                            }
                        }
                        .setTitle("Warning!")
                        .setMessage("Restore this horse?")
                        .setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_priority_high_24)).show()
                }
            }
        )
        binding.rvHorseList.adapter = adapter
        binding.rvHorseList.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.VERTICAL,false)

        binding.btnReturn.setOnClickListener {
            findNavController().navigate(R.id.action_horseArchiveListFragment_to_horseListFragment)
        }



        lifecycleScope.launchWhenCreated {
            horseListViewModel.horses
                .collect {
                    when(it){
                        is DataState.Data -> {
                            val horseListItem = it.data
                            adapter.horseList = horseListItem
                            adapter.notifyDataSetChanged()
                            Timber.v(it.toString())
                        }
                        is DataState.Error -> {
                            Timber.v(it.toString())
                        }
                        is DataState.Loading -> {
                            when(it.isLoading){
                                true -> binding.ivProgress.visibility = View.VISIBLE
                                false -> binding.ivProgress.visibility = View.INVISIBLE
                            }

                        }
                    }
                }
        }

        lifecycleScope.launchWhenStarted {
            horseListViewModel.addEventFlow.collect { event ->
                when(event){
                    is HorseArchiveListViewModel.AddEvent.Error -> {
                        Toast.makeText(requireContext(),event.errorMessage, Toast.LENGTH_SHORT).show()
                        binding.ivProgress.visibility = View.INVISIBLE
                    }
                    is HorseArchiveListViewModel.AddEvent.Loading -> {
                        if(event.isLoading){
                            binding.ivProgress.visibility = View.VISIBLE
                        }
                    }
                    is HorseArchiveListViewModel.AddEvent.Success -> {
                        Toast.makeText(requireContext(),event.message, Toast.LENGTH_SHORT).show()
                        binding.ivProgress.visibility = View.INVISIBLE
                    }
                }
            }

        }

        lifecycleScope.launchWhenCreated {
            userPreferencesFlow.collect {
                horseListViewModel.getHorses(SortOrder.BY_DATE,it.stableId)
            }
        }



    }

    override fun onDestroyView() {
        fragmenthHorseArchiveListBinding = null
        super.onDestroyView()
    }


}