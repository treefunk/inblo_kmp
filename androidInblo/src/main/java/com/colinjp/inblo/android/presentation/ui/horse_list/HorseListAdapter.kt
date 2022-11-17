package com.colinjp.inblo.android.presentation.ui.horse_list

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.colinjp.inblo.android.R
import com.colinjp.inblo.domain.model.Horse
import timber.log.Timber

class HorseListAdapter(
    var horseList: List<Horse>,
    val horseListListener: HorseListListener
): RecyclerView.Adapter<HorseListAdapter.ViewHolder>() {

    inner class ViewHolder(
        itemView: View,
        horseListListener: HorseListListener
    ): RecyclerView.ViewHolder(itemView) {
        init {

        }
        fun bind(horse: Horse, position: Int){
            itemView.findViewById<TextView>(R.id.tv_caption).text = "${position + 1}. ${horse.name}"
            itemView.findViewById<TextView>(R.id.tv_person_in_charge).text = ""
            itemView.findViewById<TextView>(R.id.tv_next_run_schedule).text = ""
            itemView.findViewById<LinearLayout>(R.id.ll_horse_list).setOnClickListener {
                horseListListener.onView(adapterPosition,horse)
            }
            itemView.findViewById<Button>(R.id.btn_view_horse).setOnClickListener {
                horseListListener.onEdit(adapterPosition,horse)
            }

            itemView.findViewById<ImageButton>(R.id.btn_archive).setOnClickListener{
                horseListListener.onArchive(adapterPosition,horse)
            }


        }


    }

    interface HorseListListener {
        fun onView(position: Int, horse: Horse)
        fun onEdit(position: Int, horse: Horse)
        fun onArchive(position: Int, horse: Horse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(R.layout.item_horse_list,parent,false),horseListListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(horseList[position],position)
    }

    override fun getItemCount(): Int {
        return horseList.size
    }
}