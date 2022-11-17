package com.colinjp.inblo.android.presentation.ui.horse_list

import android.content.Context
import android.content.DialogInterface
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colinjp.inblo.android.R
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
class HorseListFragment: Fragment(R.layout.fragment_horse_list) {

    companion object {
        const val DIALOG_TAG_ADD_HORSE = "DIALOG_ADD_HORSE"
    }

    private var fragmentHorseListBinding: FragmentHorseListBinding? = null
    private val horseListViewModel by activityViewModels<HorseListViewModel>()
    @Inject
    lateinit var userPreferencesFlow: Flow<UserPreferences>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHorseListBinding.bind(view)
        fragmentHorseListBinding = binding




        val adapter = HorseListAdapter(
            listOf(), object: HorseListAdapter.HorseListListener {
                override fun onView(position: Int, horse: Horse) {

                    HorseListFragmentDirections.actionHorseListFragmentToHorseDetailFragment(horse,false).also {
                        findNavController().navigate(it)
                    }


                }

                override fun onEdit(position: Int, horse: Horse) {
                    HorseListFragmentDirections.actionHorseListFragmentToHorseDetailFragment(horse,true).also {
                        findNavController().navigate(it)
                    }
                }

                override fun onArchive(position: Int, horse: Horse) {
                    MaterialAlertDialogBuilder(requireContext(),R.style.ThemeOverlay_App_MaterialAlertDialog)
                        .setNeutralButton("Cancel",null)
                        .setNegativeButton("ARCHIVE") { dialog, which ->
                            if (which == DialogInterface.BUTTON_NEGATIVE) {
                                horseListViewModel.archiveHorse(horse.id.toString())
                            }
                        }
                        .setTitle("Warning!")
                        .setMessage("Archive this horse?")
                        .setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_archive_24)).show()
                }
            }
        )
        binding.rvHorseList.adapter = adapter
        binding.rvHorseList.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)

        binding.btnAddHorse.setOnClickListener {
            val dialog = AddHorseDialogFragment.newInstance()
            dialog.show(childFragmentManager, DIALOG_TAG_ADD_HORSE)
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
                    is HorseListViewModel.AddEvent.Error -> {
                        Toast.makeText(requireContext(),event.errorMessage, Toast.LENGTH_SHORT).show()
                        binding.ivProgress.visibility = View.INVISIBLE
                    }
                    is HorseListViewModel.AddEvent.Loading -> {
                        if(event.isLoading){
                            binding.ivProgress.visibility = View.VISIBLE
                        }
                    }
                    is HorseListViewModel.AddEvent.Success -> {
                        Toast.makeText(requireContext(),event.message, Toast.LENGTH_SHORT).show()
                        binding.ivProgress.visibility = View.INVISIBLE
                    }
                }
            }

        }

        lifecycleScope.launchWhenCreated {
            userPreferencesFlow.collect {
                horseListViewModel.getHorses(SortOrder.BY_DATE,it.stableId)
                horseListViewModel.getUsernames(stableId = it.stableId.toString())
            }
        }



    }

    override fun onDestroyView() {
        fragmentHorseListBinding = null
        super.onDestroyView()
    }

    private fun dipToPx(dipValue: Float, context: Context): Int {
        return (dipValue * context.resources.displayMetrics.density).toInt()
    }


}