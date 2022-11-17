package com.colinjp.inblo.android.presentation.ui.horse_detail.dialogs

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.presentation.ui.BaseDialogFragment
import com.colinjp.inblo.android.presentation.ui.horse_detail.HorseDetailViewModel
import com.colinjp.inblo.domain.model.HorseDaily
import com.colinjp.inblo.util.DataState
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.EntryXComparator
import io.ktor.util.*
import kotlinx.coroutines.flow.collect
import org.joda.time.format.DateTimeFormat
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ConditionGraphDialogFragment(): BaseDialogFragment() {

    private val horseDetailViewModel by activityViewModels<HorseDetailViewModel>()

    companion object {
        fun newInstance(): ConditionGraphDialogFragment {
            val args = Bundle()
            
            val fragment = ConditionGraphDialogFragment()
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
        val view = inflater.inflate(R.layout.dialog_view_condition_graph,container,false)

        val chart = view.findViewById<LineChart>(R.id.chart_condition)
        view.findViewById<ImageButton>(R.id.ib_close_dialog).setOnClickListener { this@ConditionGraphDialogFragment.dismiss() }

        lifecycleScope.launchWhenCreated {
            horseDetailViewModel.horseDailyReports.collect { result ->
                when(result){
                    is DataState.Data -> {
                        val tempButton = view.findViewById<Button>(R.id.btn_chart_temp)
                        val weightButton = view.findViewById<Button>(R.id.btn_chart_weight)
//                        initTempGraph(chart,result.data)
                        val formattedData = result.data.data.map {
                            HorseDaily.createFromDto(it)
                        }
                        tempButton.setOnClickListener {
                            initTempGraph(chart,formattedData,GraphType.TEMP)
                        }
                        weightButton.setOnClickListener {
                            initTempGraph(chart,formattedData,GraphType.WEIGHT)
                        }
                        initTempGraph(chart,formattedData,GraphType.TEMP)

                    }
                    DataState.Empty -> { }
                    is DataState.Error -> {
                        //TODO:
                        Timber.v(result.toString())
                    }
                    is DataState.Loading -> {
                        //TODO:
                        Timber.v(result.toString())
                    }
                }
            }
        }



        return view
    }

    private fun initTempGraph(chart: LineChart, data: List<HorseDaily>, graphType: GraphType) {

        val tempValues = ArrayList<Entry>()
        val weightValues = ArrayList<Entry>()

        val df = DateTimeFormat.forPattern("yyyy-MM-dd")
        val labelListXAxis = HashMap<String,String>()


        for(horseDaily: HorseDaily in data){
            val dt = df.parseDateTime(horseDaily.date)
            val format = SimpleDateFormat("yyyy-MM-dd")
            val date = format.parse(horseDaily.date)
            val v = date.time.toFloat()
            if(horseDaily.bodyTemperature > 0.0){
                tempValues.add(Entry(v,horseDaily.bodyTemperature.toFloat()))
            }
            if(horseDaily.horseWeight > 0.0){
                weightValues.add(Entry(v,horseDaily.horseWeight.toFloat()))
            }
        }

        when(graphType){
            GraphType.TEMP -> {
                if(tempValues.isEmpty()){
                    Toast.makeText(requireContext(),"No temperature data detected.",Toast.LENGTH_SHORT).show()
                    return
                }
            }
            GraphType.WEIGHT -> {
                if(weightValues.isEmpty()){
                    Toast.makeText(requireContext(),"No Horse Weight data detected.",Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }

        val formatter = CustomValueFormatter(labelListXAxis)

        Collections.sort(tempValues,EntryXComparator())
        Collections.sort(weightValues,EntryXComparator())


        chart.apply {

            description.isEnabled = false

            setTouchEnabled(true)
            dragDecelerationFrictionCoef = 0.9f
            isDragEnabled = true
            setScaleEnabled(true)
            setDrawGridBackground(false)
            isHighlightPerDragEnabled = true
            setBackgroundColor(Color.TRANSPARENT)
//            setViewPortOffsets(0f, 0f, 0f, 0f)



            xAxis.apply {
                valueFormatter = formatter
                position = XAxis.XAxisPosition.BOTTOM
                textSize = 8f
                setDrawAxisLine(false)
                setDrawGridLines(true)
                textColor = Color.rgb(0,0,0)
                fitScreen()
                setAvoidFirstLastClipping(true)
            }
            axisLeft.apply {
                setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)

//                yOffset = 10f
            }

            axisRight.apply {
                isEnabled = false
            }
        }



        // create a dataset and give it a type
        val set1 = LineDataSet(tempValues, "体温")
//        set1.axisDependency = AxisDependency.LEFT

        set1.setDrawCircles(true)
        set1.setDrawValues(true)
        set1.setDrawCircleHole(true)

        val colPrimary = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
        val colPrimaryDark = ContextCompat.getColor(requireContext(),R.color.colorPrimaryDark)

        set1.color = colPrimaryDark
        set1.setCircleColor(colPrimaryDark)
        set1.circleHoleColor = colPrimary
        set1.circleRadius = 3f
        set1.circleHoleRadius = 2f

        set1.valueTextSize = 10f
        set1.lineWidth = 1.5f
        set1.fillAlpha = 110

        val set2 = LineDataSet(weightValues, "馬体重")
        set2.setDrawCircles(true)
        set2.setDrawValues(true)
        set2.setDrawCircleHole(true)
//        set2.axisDependency = AxisDependency.LEFT
        set2.color = colPrimaryDark
        set2.setCircleColor(colPrimaryDark)
        set2.circleHoleColor = colPrimary
        set2.circleRadius = 3f
        set2.circleHoleRadius = 2f


        set2.lineWidth = 1.5f
        set2.fillAlpha = 110


        // create a data object with the data sets
        val data = when(graphType){
            GraphType.TEMP -> {
                chart.xAxis.apply {
                    labelCount = tempValues.size + 1
                    axisMaximum = tempValues.last().x
                }
                chart.axisLeft.apply {
                    axisMinimum = 35.0f
                    axisMaximum = 42.0f
                    granularity = 1f
                }
                LineData(set1)
            }
            GraphType.WEIGHT -> {
                chart.xAxis.apply {
                    labelCount = weightValues.size + 1
                    axisMaximum = weightValues.last().x
                }
                chart.axisLeft.apply {
                    granularity = 1f
                    resetAxisMaximum()
                    resetAxisMinimum()
                }
                LineData(set2)
            }
        }
        data.setValueTextColor(ContextCompat.getColor(requireContext(),R.color.colorBlackText))
        data.setValueTextSize(10f)

        // set data
        chart.data = data
        chart.animateX(1000, Easing.EaseOutQuad)
        chart.invalidate()

    }


}

class CustomValueFormatter(val listLabel: HashMap<String, String>): ValueFormatter() {
    private val mFormat = SimpleDateFormat("dd MMM", Locale.ENGLISH)

//    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
//       val dt = DateTime(value.toLong())
//        Timber.v("value ${dt.toString("yyyy-MM-dd")}\n")
//        return dt.toString("yyyy-MM-dd")
//    }

    override fun getFormattedValue(value: Float): String {
        val milli = value.toLong()
        return mFormat.format(Date(milli));
    }
}

enum class GraphType {
    TEMP(),
    WEIGHT()
}
