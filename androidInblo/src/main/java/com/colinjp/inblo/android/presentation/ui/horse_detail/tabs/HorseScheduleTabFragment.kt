package com.colinjp.inblo.android.presentation.ui.horse_detail.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.databinding.FragmentTabHorseScheduleBinding

class HorseScheduleTabFragment: Fragment(R.layout.fragment_tab_horse_schedule) {
    private var horseScheduleBinding: FragmentTabHorseScheduleBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTabHorseScheduleBinding.bind(view)
        horseScheduleBinding = binding

    }

    override fun onDestroyView() {
        horseScheduleBinding = null
        super.onDestroyView()
    }
}