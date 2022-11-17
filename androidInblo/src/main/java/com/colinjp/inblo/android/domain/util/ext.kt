package com.colinjp.inblo.android.domain.util

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.text.InputType
import android.widget.ArrayAdapter
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.domain.model.EventType
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.DayOfWeek
import java.time.temporal.WeekFields
import java.util.*


fun DateTime.toReadableDateTime(): String {
//    val pattern = "MMMM dd, yyyy"
    val pattern = "yyyy-MM-dd"
    return DateTimeFormat.forPattern(pattern).print(this)
}

fun TextInputEditText.initDatePickerSpinner(
    context: Context,
    requestFocus: Boolean = true,
    minDate:  Long? = null,
    onSelectDate: (() -> Unit)? = null
){

    this.setOnClickListener {
        val dtNow = if (this.text.toString().isEmpty()) {
            DateTime()
        } else {
            DateTime.parse(this.text.toString(), DateTimeFormat.forPattern("yyyy-MM-dd"))
        }
        val dp = DatePickerDialog(context, R.style.MySpinnerDatePickerStyle, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val dt = DateTimeFormat.forPattern("yyyy M d").parseDateTime("$year ${month + 1} $dayOfMonth")
            this.setText(dt.toReadableDateTime())
            onSelectDate?.invoke()
        },dtNow.year,dtNow.monthOfYear - 1,dtNow.dayOfMonth)
        if(minDate != null){
            dp.datePicker.minDate = minDate
        }
        dp.show()
    }

    if(requestFocus){
        this.apply {
            requestFocus()
            setOnTouchListener { v, event ->
                val inType = this.inputType
                inputType = InputType.TYPE_NULL
                this.onTouchEvent(event)
                true
            }
        }
    }

}

fun String.parseEventType(): EventType {
    return EventType.values().first { it.type == this }
}

fun String.ifBlankZero(): String {
    return if(this.isBlank()) "0.0" else this
}

fun Double.displayBlankIfZero(): String{
    if(this == 0.0){
        return ""
    }
    return this.toString()
}

fun daysOfWeekFromLocale(): Array<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()
    // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
    // Only necessary if firstDayOfWeek != DayOfWeek.MONDAY which has ordinal 0.
    if (firstDayOfWeek != DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek
}

fun MaterialAutoCompleteTextView.initChoices(context: Context, choices: List<String>, onSelectOption: ((selectedOption: String, index: Int) -> Unit)? = null,autoDropDown: Boolean = true) {
    val adapter: ArrayAdapter<String> = ArrayAdapter(
        context,android.R.layout.simple_dropdown_item_1line,
        listOf("") + choices
    )
    this.apply {
        setAdapter(adapter)
        if(autoDropDown){
            setOnClickListener {
                showDropDown()
            }
        }
        setOnItemClickListener { parent, view, position, id ->
            if(position != 0){
                this.setText(choices[position - 1],false)
            }else{
                this.setText("")
            }
            onSelectOption?.invoke(
                if(position != 0) choices[position - 1] else "", position
            )
        }
    }
}


fun Uri.fetchFile(context: Context): File {
    val dir = File(context.filesDir,"uploaded_daily")
    if (!dir.exists()) {
        dir.mkdirs()
    }
    val destinationFileName = File(dir,this.queryName(context))
    try {
        context.contentResolver.openInputStream(this).use { ins ->
            createFileFromStream(
                ins!!,
                destinationFileName
            )
        }
    } catch (ex: Exception) {
        Timber.e("Save File ${ex.message}")
        ex.printStackTrace()
    }
    return destinationFileName
}




fun Uri.createFileFromStream(inputStream: InputStream, destination: File){

    try {
        FileOutputStream(destination).use { os ->
            val buffer = ByteArray(4096)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                os.write(buffer, 0, length)
            }
            os.flush()
        }
    } catch (ex: Exception) {
        Timber.e("Save File ${ex.message}")
        ex.printStackTrace()
    }

}

fun Uri.queryName(context: Context): String {
    val returnCursor = context.contentResolver.query(this,null,null,null,null)!!
    val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor.moveToFirst()
    val name = returnCursor.getString(nameIndex)
    returnCursor.close()
    return name
}