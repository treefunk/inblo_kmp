package com.colinjp.inblo.android.presentation.ui.horse_detail.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.databinding.DialogViewHorseBinding
import com.colinjp.inblo.android.presentation.ui.BaseDialogFragment
import com.colinjp.inblo.android.presentation.ui.horse_list.HorseListViewModel
import com.colinjp.inblo.android.presentation.ui.horse_list.SortOrder
import com.colinjp.inblo.domain.model.Horse
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewHorseDialogFragment: BaseDialogFragment() {

    private val horseListViewModel by activityViewModels<HorseListViewModel>()
    private var _binding: DialogViewHorseBinding? = null

    companion object {
        const val HORSE_ARG = "horse_arg"
        fun newInstance(horse: Horse): ViewHorseDialogFragment {
            val args = Bundle()
            args.putParcelable(HORSE_ARG,horse)
            val fragment = ViewHorseDialogFragment()
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
//        val view = inflater.inflate(R.layout.dialog_view_horse,container,false)
        val binding = DialogViewHorseBinding.inflate(inflater,container,false)
        _binding = binding

        val horse = requireArguments().getParcelable<Horse>(HORSE_ARG)!!

        binding.ibCloseDialog.setOnClickListener {
            this@ViewHorseDialogFragment.dismiss()
        }

        horse.let {
            binding.tvHorseName.text = it.name
            binding.tvPersonInCharge.text = it.user?.username
            binding.tvStableName.text = it.stable?.name
            binding.tvOwnerName.text = it.ownerName
            binding.tvFarmName.text = it.farmName
            binding.tvTrainingFarmName.text = it.trainingFarmName
            binding.tvColor.text = it.color
            binding.tvTotalStake.text = it.totalStake.toString()
            val age = if(it.age != -1) it.age else ""
            val sexualAge = it.sex + age
            binding.tvSexualAge.text =  sexualAge
            binding.tvFather.text = it.fatherName
            binding.tvMother.text = it.motherName
            binding.tvMofa.text = it.motherFatherName
            binding.tvClass.text = it.class_
            binding.tvMemo.text = it.notes
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}