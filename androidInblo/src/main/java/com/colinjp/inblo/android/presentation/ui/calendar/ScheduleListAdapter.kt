package com.colinjp.inblo.android.presentation.ui.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.domain.util.parseEventType
import com.colinjp.inblo.datasource.network.calendar.EventDTO
import com.colinjp.inblo.domain.model.Event
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat


class ScheduleListAdapter (
    var eventList: List<Event>,
    val eventScheduleListener: EventScheduleListener? = null
): RecyclerView.Adapter<ScheduleListAdapter.ViewHolder>() {

    inner class ViewHolder(
        itemView: View,
        eventScheduleListener: EventScheduleListener?
    ): RecyclerView.ViewHolder(itemView) {
        init {

        }
        fun bind(event: Event, position: Int){

            val eventType = event.eventType.parseEventType()

            itemView.findViewById<View>(R.id.rect_left).setBackgroundColor(ContextCompat.getColor(itemView.context,eventType.colorDark))
            itemView.findViewById<View>(R.id.tv_time).setBackgroundColor(ContextCompat.getColor(itemView.context,eventType.colorLight))
            itemView.findViewById<View>(R.id.tv_status).backgroundTintList = ContextCompat.getColorStateList(itemView.context,eventType.colorDarker)

            val pattern =  DateTimeFormat.forPattern("Y-m-d")

            itemView.findViewById<TextView>(R.id.tv_time).text = DateTime.parse(event.date,pattern).toString("mm/dd")
            itemView.findViewById<TextView>(R.id.tv_title).text = event.title
            itemView.findViewById<TextView>(R.id.tv_caption).text = event.horse?.name

            itemView.findViewById<TextView>(R.id.tv_status).setOnClickListener {
                eventScheduleListener?.onView(adapterPosition,event)
            }

        }
    }

    interface EventScheduleListener {
        fun onView(position: Int, event: Event)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(R.layout.item_schedule,parent,false), eventScheduleListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(eventList[position],position)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}