package com.colinjp.inblo.android.presentation.ui.horse_detail.tabs

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.domain.util.displayBlankIfZero
import com.colinjp.inblo.domain.model.HorseDaily
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class HorseDailyAdapter (
    var horseDailyList: List<HorseDaily>,
    val horseDailyListener: HorseDailyListener,
    var hiddenColumns: String = ""
): RecyclerView.Adapter<HorseDailyAdapter.ViewHolder>() {

    inner class ViewHolder(
        itemView: View,
        horseDailyListener: HorseDailyListener
    ) : RecyclerView.ViewHolder(itemView) {
        init {

        }

        fun bind(horseDaily: HorseDaily) {
            val pattern =  DateTimeFormat.forPattern("Y-m-d")


            itemView.findViewById<TextView>(R.id.tv_date).visibility = if(hiddenColumns.contains("date")) View.GONE else View.VISIBLE
            itemView.findViewById<TextView>(R.id.tv_temp).visibility = if(hiddenColumns.contains("body_temperature")) View.GONE else View.VISIBLE
            itemView.findViewById<TextView>(R.id.tv_weight).visibility = if(hiddenColumns.contains("horse_weight")) View.GONE else View.VISIBLE
            itemView.findViewById<TextView>(R.id.tv_condition_group).visibility = if(hiddenColumns.contains("condition_group")) View.GONE else View.VISIBLE
            itemView.findViewById<TextView>(R.id.tv_rider_name).visibility = if(hiddenColumns.contains("rider")) View.GONE else View.VISIBLE
            itemView.findViewById<TextView>(R.id.tv_training_type).visibility = if(hiddenColumns.contains("training_type")) View.GONE else View.VISIBLE
            itemView.findViewById<TextView>(R.id.tv_training_amount).visibility = if(hiddenColumns.contains("training_amount")) View.GONE else View.VISIBLE
            itemView.findViewById<TextView>(R.id.tv_5f).visibility = if(hiddenColumns.contains("5f_time")) View.GONE else View.VISIBLE
            itemView.findViewById<TextView>(R.id.tv_4f).visibility = if(hiddenColumns.contains("4f_time")) View.GONE else View.VISIBLE
            itemView.findViewById<TextView>(R.id.tv_3f).visibility = if(hiddenColumns.contains("3f_time")) View.GONE else View.VISIBLE
            itemView.findViewById<TextView>(R.id.tv_notes).visibility = if(hiddenColumns.contains("memo")) View.GONE else View.VISIBLE


            //data
            itemView.findViewById<TextView>(R.id.tv_date).text = DateTime.parse(horseDaily.date,pattern).toString("Ymmdd")
            itemView.findViewById<TextView>(R.id.tv_temp).text = horseDaily.bodyTemperature.displayBlankIfZero().let { if(it == "") "" else "$it°C"}
            itemView.findViewById<TextView>(R.id.tv_weight).text = horseDaily.horseWeight.displayBlankIfZero()
            itemView.findViewById<TextView>(R.id.tv_condition_group).text = horseDaily.conditionGroup
            itemView.findViewById<TextView>(R.id.tv_rider_name).text = horseDaily.rider?.name
            itemView.findViewById<TextView>(R.id.tv_training_type).text = horseDaily.trainingType?.name
            itemView.findViewById<TextView>(R.id.tv_training_amount).text = horseDaily.trainingAmount
            itemView.findViewById<TextView>(R.id.tv_5f).text = horseDaily.time5f.displayBlankIfZero()
            itemView.findViewById<TextView>(R.id.tv_4f).text = horseDaily.time4f.displayBlankIfZero()
            itemView.findViewById<TextView>(R.id.tv_3f).text = horseDaily.time3f.displayBlankIfZero()
            itemView.findViewById<TextView>(R.id.tv_notes).text = horseDaily.memo
            itemView.findViewById<ImageButton>(R.id.btn_edit).setOnClickListener {
                horseDailyListener.onEdit(adapterPosition,horseDaily)
            }
            itemView.findViewById<ImageButton>(R.id.btn_trash).setOnClickListener {
                horseDailyListener.onDelete(adapterPosition,horseDaily)
            }
            if(horseDaily.attachedFiles != null && horseDaily.attachedFiles!!.isNotEmpty()){
                itemView.findViewById<TextView>(R.id.tv_attachments).apply {
                    setOnClickListener {
                        horseDailyListener.onViewFiles(horseDaily)
                    }
                    visibility = View.VISIBLE
                }
            }

        }
    }

    interface HorseDailyListener {
        fun onEdit(position: Int, horseDaily: HorseDaily)
        fun onDelete(position: Int, horseDaily: HorseDaily)
        fun onViewFiles(horseDaily: HorseDaily)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(R.layout.item_horse_daily, parent, false), horseDailyListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(horseDailyList[position])
    }

    override fun getItemCount(): Int {
        return horseDailyList.size
    }
}