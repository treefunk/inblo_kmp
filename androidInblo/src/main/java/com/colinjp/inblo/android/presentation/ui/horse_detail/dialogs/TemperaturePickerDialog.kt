package com.colinjp.inblo.android.presentation.ui.horse_detail.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.presentation.ui.BaseDialogFragment

class TemperaturePickerDialog: BaseDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.dialog_temperature_picker,container,false)
        val firstNum = view.findViewById<NumberPicker>(R.id.np_temp)
        val secondNum = view.findViewById<NumberPicker>(R.id.np_temp_decimal)
        firstNum.minValue = 35
        firstNum.maxValue = 42

        return view
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}