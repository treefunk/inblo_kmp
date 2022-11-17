package com.colinjp.inblo.android.presentation.ui.horse_detail.tabs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.colinjp.inblo.android.R
import com.colinjp.inblo.domain.model.HorseTreatment
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class HorseTreatmentsAdapter (
    var horseTreatmentList: List<HorseTreatment>,
    val horseTreatmentListener: HorseTreatmentListener
): RecyclerView.Adapter<HorseTreatmentsAdapter.ViewHolder>() {

    inner class ViewHolder(
        itemView: View,
        horseTreatmentListener: HorseTreatmentListener
    ) : RecyclerView.ViewHolder(itemView) {
        init {

        }

        fun bind(horseTreatment: HorseTreatment) {
            val pattern =  DateTimeFormat.forPattern("Y-m-d")

            itemView.findViewById<TextView>(R.id.tv_date).text = DateTime.parse(horseTreatment.date,pattern).toString("Ymmdd")
            itemView.findViewById<TextView>(R.id.tv_vet_name).text = horseTreatment.doctorName
            itemView.findViewById<TextView>(R.id.tv_contents).text = horseTreatment.treatmentDetail
            itemView.findViewById<TextView>(R.id.tv_injured_part).text = horseTreatment.injuredPart
            itemView.findViewById<TextView>(R.id.tv_occasion_type).text = horseTreatment.occasionType
            itemView.findViewById<TextView>(R.id.tv_drug_name).text = horseTreatment.medicineName
            itemView.findViewById<TextView>(R.id.tv_notes).text = horseTreatment.memo
            itemView.findViewById<ImageButton>(R.id.btn_edit).setOnClickListener {
                horseTreatmentListener.onEdit(adapterPosition,horseTreatment)
            }
            itemView.findViewById<ImageButton>(R.id.btn_trash).setOnClickListener {
                horseTreatmentListener.onDelete(adapterPosition,horseTreatment)
            }

            if(horseTreatment.attachedFiles != null && horseTreatment.attachedFiles!!.isNotEmpty()){
                itemView.findViewById<TextView>(R.id.tv_attachments).apply {
                    setOnClickListener {
                        horseTreatmentListener.onViewFiles(horseTreatment)
                    }
                    visibility = View.VISIBLE
                }
            }
        }
    }

    interface HorseTreatmentListener {
        fun onEdit(position: Int, horseTreatment: HorseTreatment)
        fun onDelete(position: Int, horseTreatment: HorseTreatment)
        fun onViewFiles(horseTreatment: HorseTreatment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(R.layout.item_horse_treatment, parent, false), horseTreatmentListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(horseTreatmentList[position])
    }

    override fun getItemCount(): Int {
        return horseTreatmentList.size
    }
}