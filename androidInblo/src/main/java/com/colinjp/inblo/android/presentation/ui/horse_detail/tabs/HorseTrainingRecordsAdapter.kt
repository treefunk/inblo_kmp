package com.colinjp.inblo.android.presentation.ui.horse_detail.tabs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.domain.util.displayBlankIfZero
import com.colinjp.inblo.domain.model.HorseTrainingRecord

class HorseTrainingRecordsAdapter (
    var horseTrainingRecordList: List<HorseTrainingRecord>,
    val horseTrainingRecordListener: HorseTrainingRecordListener
): RecyclerView.Adapter<HorseTrainingRecordsAdapter.ViewHolder>() {

    inner class ViewHolder(
        itemView: View,
        horseTrainingRecordListener: HorseTrainingRecordListener
    ) : RecyclerView.ViewHolder(itemView) {
        init {

        }

        fun bind(horseTrainingRecord: HorseTrainingRecord) {
            itemView.findViewById<TextView>(R.id.tv_date).text = horseTrainingRecord.date
            itemView.findViewById<TextView>(R.id.tv_weather).text = horseTrainingRecord.weather
            itemView.findViewById<TextView>(R.id.tv_type).text = horseTrainingRecord.trainingType
            itemView.findViewById<TextView>(R.id.tv_training_detail).text = horseTrainingRecord.trainingDetail
            itemView.findViewById<TextView>(R.id.tv_6f).text = horseTrainingRecord.time6F.displayBlankIfZero()
            itemView.findViewById<TextView>(R.id.tv_5f).text = horseTrainingRecord.time5F.displayBlankIfZero()
            itemView.findViewById<TextView>(R.id.tv_4f).text = horseTrainingRecord.time4F.displayBlankIfZero()
            itemView.findViewById<TextView>(R.id.tv_3f).text = horseTrainingRecord.time3F.displayBlankIfZero()
            itemView.findViewById<TextView>(R.id.tv_2f).text = horseTrainingRecord.time2F.displayBlankIfZero()
            itemView.findViewById<TextView>(R.id.tv_1f).text = horseTrainingRecord.time1F.displayBlankIfZero()
            itemView.findViewById<TextView>(R.id.tv_notes).text = horseTrainingRecord.memo
            itemView.findViewById<ImageButton>(R.id.btn_edit).setOnClickListener {
                horseTrainingRecordListener.onEdit(adapterPosition,horseTrainingRecord)
            }
            itemView.findViewById<ImageButton>(R.id.btn_trash).setOnClickListener {
                horseTrainingRecordListener.onDelete(adapterPosition,horseTrainingRecord)
            }
        }
    }

    interface HorseTrainingRecordListener {
        fun onEdit(position: Int, horseTrainingRecord: HorseTrainingRecord)
        fun onDelete(position: Int, horseTrainingRecord: HorseTrainingRecord)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(R.layout.item_horse_training, parent, false), horseTrainingRecordListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(horseTrainingRecordList[position])
    }

    override fun getItemCount(): Int {
        return horseTrainingRecordList.size
    }
}