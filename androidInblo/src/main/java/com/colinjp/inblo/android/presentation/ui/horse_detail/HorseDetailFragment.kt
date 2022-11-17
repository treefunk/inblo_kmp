package com.colinjp.inblo.android.presentation.ui.horse_detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.databinding.FragmentHorseDetailBinding
import com.colinjp.inblo.android.domain.util.UserPreferences
import com.colinjp.inblo.android.presentation.ui.calendar.CalendarFragment
import com.colinjp.inblo.android.presentation.ui.horse_detail.dialogs.ViewHorseDialogFragment
import com.colinjp.inblo.android.presentation.ui.horse_list.AddHorseDialogFragment
import com.colinjp.inblo.android.presentation.ui.horse_list.HorseListViewModel
import com.colinjp.inblo.android.presentation.ui.horse_list.SortOrder
import com.colinjp.inblo.domain.model.Horse
import com.colinjp.inblo.util.DataState

import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class HorseDetailFragment: Fragment(R.layout.fragment_horse_detail) {

    companion object {
        const val DIALOG_TAG_VIEW_HORSE = "DIALOG_TAG_VIEW_HORSE"
        const val DIALOG_EDIT_HORSE = "DIALOG_EDIT_HORSE"
        const val REQUEST_EDIT_HORSE = "REQUEST_EDIT_HORSE"
    }

    private var fragmentHorseDetailBinding: FragmentHorseDetailBinding? = null

    private val horseDetailViewModel by activityViewModels<HorseDetailViewModel>()
    private val horseListViewModel by activityViewModels<HorseListViewModel>()
    @Inject
    lateinit var userPreferencesFlow: Flow<UserPreferences>

    internal val args: HorseDetailFragmentArgs by navArgs()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHorseDetailBinding.bind(view)
        fragmentHorseDetailBinding = binding


        val horse = args.horse
        horse.let {
            horseDetailViewModel.initHorse(it.id!!)
        }
        horse.let {
            binding.tvHorse.text = it.name
            binding.tvClass.text = it.class_
            binding.tvFather.text = it.fatherName
            binding.tvMother.text = it.motherName
            binding.tvMofa.text = it.motherFatherName
            val age = if(it.age != -1) it.age else ""
            val sexualAge = it.sex + age
            if(sexualAge.isNotBlank()){
                binding.tvHorseAge.visibility = View.VISIBLE
                binding.tvHorseAge.text =  "${it.sex}${age}"
            }else{
                binding.tvHorseAge.visibility = View.INVISIBLE
            }

            binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let { tab ->
                        val tab = indexToHorseTab(tab.position)
                        val navController = requireActivity().findNavController(R.id.nav_host_fragment_horse_detail)
                        when(tab){
                            HorseTabs.CONDITION -> navController.navigate(R.id.action_global_horseConditionTabFragment)
                            HorseTabs.TREATMENT -> navController.navigate(R.id.action_global_horseTreatmentTabFragment)
                            HorseTabs.CALENDAR -> navController.navigate(R.id.action_global_calendarFragment2,Bundle().also { b ->
                                b.putParcelable(CalendarFragment.ARGS_HORSE,it)
                            })

                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })
        }

        val isEditMode = args.isEdit






        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnViewHorseProfile.setOnClickListener {
            val dialog = ViewHorseDialogFragment.newInstance(horse)
            dialog.show(childFragmentManager, DIALOG_TAG_VIEW_HORSE)
        }

        binding.ibEdit.setOnClickListener {
            showEditHorseDialog(horse,binding)
        }


//        horseDetailViewModel.fetchDropDownData()


        lifecycleScope.launchWhenStarted {
            horseDetailViewModel.addEventFlow.onEach { event ->
                when(event){
                    is HorseDetailViewModel.AddEvent.Error -> {
                        Toast.makeText(requireContext(),event.errorMessage, Toast.LENGTH_SHORT).show()
                        binding.ivProgress.visibility = View.INVISIBLE
                    }
                    is HorseDetailViewModel.AddEvent.Loading -> {
                        if(event.isLoading){
                            binding.ivProgress.visibility = View.VISIBLE
                        }else{
                            binding.ivProgress.visibility = View.INVISIBLE
                        }
                    }
                    is HorseDetailViewModel.AddEvent.Success -> {
                        Toast.makeText(requireContext(),event.message, Toast.LENGTH_SHORT).show()
                        horseDetailViewModel.initHorse(horse.id!!)
                        binding.ivProgress.visibility = View.INVISIBLE
                    }
                }
            }.launchIn(lifecycleScope)
        }


        lifecycleScope.launchWhenStarted {
            horseListViewModel.updateEventFlow.collect { event ->
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

        if(isEditMode){
            showEditHorseDialog(horse,binding)
        }

        lifecycleScope.launchWhenCreated {
            userPreferencesFlow.collect {
                horseDetailViewModel.fetchDropDownData(it.stableId.toString())
            }
        }



    }

    private fun showEditHorseDialog(horse: Horse,binding: FragmentHorseDetailBinding){
        val dialog = AddHorseDialogFragment.newInstance(horse)
        childFragmentManager.setFragmentResultListener(REQUEST_EDIT_HORSE,viewLifecycleOwner){ key, bundle ->
            if(key == REQUEST_EDIT_HORSE){
                val newHorse = bundle.getParcelable<Horse>(AddHorseDialogFragment.BUNDLE_KEY_HORSE)
                if(newHorse != null ){
                    binding.tvHorse.setText(newHorse.name)

                    binding.btnViewHorseProfile.setOnClickListener {
                        val dialog = ViewHorseDialogFragment.newInstance(newHorse)
                        dialog.show(childFragmentManager, DIALOG_TAG_VIEW_HORSE)
                    }

                    binding.ibEdit.setOnClickListener {
                        showEditHorseDialog(newHorse,binding)
                    }
                }
            }
        }
        dialog.show(childFragmentManager, DIALOG_TAG_VIEW_HORSE)
    }


    override fun onDestroyView() {
        fragmentHorseDetailBinding = null
        super.onDestroyView()
    }

    enum class HorseTabs {
        CONDITION(),
        TREATMENT(),
        CALENDAR()
    }

    private fun indexToHorseTab(index: Int): HorseTabs{
        return when(index){
            0 -> HorseTabs.CONDITION
            1 -> HorseTabs.TREATMENT
            2 -> HorseTabs.CALENDAR
            else -> throw ArrayIndexOutOfBoundsException()
        }
    }
}
