package com.colinjp.inblo.android.presentation.ui.horse_list_archive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.colinjp.inblo.android.R
import com.colinjp.inblo.domain.model.Horse

class HorseArchiveListAdapter(
    var horseList: List<Horse>,
    val horseListListener: HorseArchiveListListener
): RecyclerView.Adapter<HorseArchiveListAdapter.ViewHolder>() {

    inner class ViewHolder(
        itemView: View,
        horseListListener: HorseArchiveListListener
    ): RecyclerView.ViewHolder(itemView) {
        init {

        }
        fun bind(horse: Horse, position: Int){
            itemView.findViewById<TextView>(R.id.tv_caption).text = "${position + 1}. ${horse.name}"
            itemView.findViewById<TextView>(R.id.tv_person_in_charge).text = ""
            itemView.findViewById<TextView>(R.id.tv_next_run_schedule).text = ""

            itemView.findViewById<Button>(R.id.btn_restore_horse).setOnClickListener{
                horseListListener.onRestore(adapterPosition,horse)
            }


        }


    }

    interface HorseArchiveListListener {
        fun onRestore(position: Int, horse: Horse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(R.layout.item_horse_archive_list,parent,false),horseListListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(horseList[position],position)
    }

    override fun getItemCount(): Int {
        return horseList.size
    }
}