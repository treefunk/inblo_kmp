package com.colinjp.inblo.android.presentation.ui.horse_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.databinding.DialogAddHorseBinding
import com.colinjp.inblo.android.di.dataStore
import com.colinjp.inblo.android.domain.util.PreferenceKeys
import com.colinjp.inblo.android.domain.util.UserPreferences
import com.colinjp.inblo.android.domain.util.initChoices
import com.colinjp.inblo.android.domain.util.initDatePickerSpinner
import com.colinjp.inblo.android.presentation.ui.BaseDialogFragment
import com.colinjp.inblo.android.presentation.ui.horse_detail.HorseDetailFragment
import com.colinjp.inblo.domain.model.Horse
import com.colinjp.inblo.util.DataState
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AddHorseDialogFragment: BaseDialogFragment() {

    private val horseListViewModel by activityViewModels<HorseListViewModel>()
    private var _binding: DialogAddHorseBinding? = null
    @Inject
    lateinit var userPreferencesFlow: Flow<UserPreferences>

    companion object {
        const val ARGS_HORSE = "ARGS_HORSE"
        const val BUNDLE_KEY_HORSE = "BUNDLE_KEY_HORSE"

        fun newInstance(horse: Horse? = null): AddHorseDialogFragment {
            val args = Bundle()
            args.putParcelable(ARGS_HORSE,horse)
            val fragment = AddHorseDialogFragment()
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
        val binding = DialogAddHorseBinding.inflate(inflater,container,false)
        _binding = binding

        val horse = requireArguments().getParcelable<Horse>(ARGS_HORSE)

        binding.ibCloseDialog.setOnClickListener {
            this@AddHorseDialogFragment.dismiss()
        }

        binding.etSex.initChoices(requireContext(), listOf("牡","牝","騙"))
        binding.etColor.initChoices(requireContext(), listOf("鹿毛","黒鹿毛","青鹿毛","青毛","芦毛","栗毛","栃栗毛","白毛"))
        binding.etBirthDate.initDatePickerSpinner(requireContext(), requestFocus = false)

        if(horse != null){
            binding.etHorseName.setText(horse.name)
            binding.acPersonInCharge.apply {
                setText(horse.user?.username)
                if(horse.user?.id != null){
                    tag = horse.user?.id.toString()
                }else{
                    tag = "0"
                }
            }
            binding.acStableName.apply {
                setText(horse.stable?.name)
                if(horse.stable != null){
                    tag = horse.stable?.id.toString()
                }else{
                    tag = "0"
                }
            }
            binding.etOwnerName.setText(horse.ownerName)
            binding.etFarmName.setText(horse.farmName)
            binding.etTrainingFarmName.setText(horse.trainingFarmName)
            binding.etBirthDate.setText(horse.birthDate)
            binding.etColor.setText(horse.color)
            binding.etSex.setText(horse.sex)
            binding.etFather.setText(horse.fatherName)
            binding.etMother.setText(horse.motherName)
            binding.etMofa.setText(horse.motherFatherName)
            binding.etClass.setText(horse.class_)
            binding.etTotalStake.setText(horse.totalStake.toString())
            binding.etNotes.setText(horse.notes)
        }

        lifecycleScope.launchWhenCreated {
            horseListViewModel.users.collect { users ->
                when(users){
                    is DataState.Data -> {
                        binding.acPersonInCharge.initChoices(
                            requireContext(),
                            users.data.map { it.username.toString() }, { _,pos ->
                                if(pos != 0){
                                    binding.acPersonInCharge.tag = users.data[pos - 1].id.toString()
                                }else{
                                    binding.acPersonInCharge.tag = ""
                                }

                            }
                        )
                        Timber.v("users-> $users")
                    }
                    DataState.Empty -> { }
                    is DataState.Error -> {
                        Timber.e(users.exception)
                    }
                    is DataState.Loading -> { }
                }
            }
        }



        binding.btnAddHorse.setOnClickListener {
            val horseName = binding.etHorseName.text.toString()
            val personInCharge = binding.acPersonInCharge.tag.toString()
            val ownerName = binding.etOwnerName.text.toString()
            val farmName = binding.etFarmName.text.toString()
            val trainingFarmName = binding.etTrainingFarmName.text.toString()
            val color = binding.etColor.text.toString()
            val sexualAge = binding.etSex.text.toString()
            val father = binding.etFather.text.toString()
            val mother = binding.etMother.text.toString()
            val motherFather = binding.etMofa.text.toString()
            val _class = binding.etClass.text.toString()
            val birthDate = binding.etBirthDate.text.toString()
            val totalStake = if(binding.etTotalStake.text.toString().isEmpty()) 0.0 else binding.etTotalStake.text.toString().toDouble()
            val notes = binding.etNotes.text.toString()


            if(horseName.isBlank()){
                Toast.makeText(requireContext(),"馬名を入力してください。", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            lifecycleScope.launch(IO){
                userPreferencesFlow.collect { userPreferences ->
                    if(horse == null){
                        horseListViewModel.addHorse(
                            horseName,
                            personInCharge,
                            userPreferences.stableId,
                            ownerName,
                            farmName,
                            trainingFarmName,
                            sexualAge,
                            color,
                            _class,
                            birthDate,
                            father,
                            mother,
                            motherFather,
                            totalStake,
                            notes,
                            null
                        )
                    }else{



                        horseListViewModel.addHorse(
                            horseName,
                            personInCharge,
                            userPreferences.stableId,
                            ownerName,
                            farmName,
                            trainingFarmName,
                            sexualAge,
                            color,
                            _class,
                            birthDate,
                            father,
                            mother,
                            motherFather,
                            totalStake,
                            notes,
                            horse.id
                        )
                        withContext(Main){
                            val bundle = Bundle().also {
                                it.putParcelable(BUNDLE_KEY_HORSE,Horse(
                                    horse.id,
                                    userPreferences.stableId,
                                    horse.farmId,
                                    horse.trainingFarmId,
                                    horse.user,
                                    horse.stable,
                                    horse.ownerName,
                                    horse.farmName,
                                    horse.trainingFarmName,
                                    birthDate,
                                    sexualAge,
                                    horse.age,
                                    horseName,
                                    color,
                                    _class,
                                    father,
                                    mother,
                                    motherFather,
                                    totalStake,
                                    notes,
                                    horse.updatedAt,
                                    horse.createdAt
                                ))
                            }
                            parentFragmentManager.setFragmentResult(HorseDetailFragment.REQUEST_EDIT_HORSE,bundle)
                        }
                    }

                    this@AddHorseDialogFragment.dismiss()
                }

            }


        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}