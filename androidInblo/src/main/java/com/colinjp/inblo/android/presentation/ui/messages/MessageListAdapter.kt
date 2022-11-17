package com.colinjp.inblo.android.presentation.ui.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.colinjp.inblo.android.R
import com.colinjp.inblo.domain.model.Message

class MessageListAdapter(
    var messageList: List<Message>,
    val userId: Int,
    val messageListListener: MessageListListener,
): RecyclerView.Adapter<MessageListAdapter.ViewHolder>() {

    inner class ViewHolder(
        itemView: View,
        messageListListener: MessageListListener
    ): RecyclerView.ViewHolder(itemView) {
        init {

        }
        fun bind(message: Message, position: Int){

            if(message.sender?.id == userId){
                itemView.findViewById<ImageButton>(R.id.btn_edit).visibility = View.VISIBLE
                itemView.findViewById<ImageButton>(R.id.btn_trash).visibility = View.VISIBLE
            }else{
                itemView.findViewById<ImageButton>(R.id.btn_edit).visibility = View.INVISIBLE
                itemView.findViewById<ImageButton>(R.id.btn_trash).visibility = View.INVISIBLE
            }

            itemView.findViewById<TextView>(R.id.tv_date).text = message.formattedDate
            itemView.findViewById<TextView>(R.id.tv_time).text = message.formattedTime
            itemView.findViewById<TextView>(R.id.tv_from).text = message.sender?.username
            itemView.findViewById<TextView>(R.id.tv_to).text = message.recipient?.username ?: "全員"
            itemView.findViewById<TextView>(R.id.tv_title).text = message.title
            itemView.findViewById<TextView>(R.id.tv_type).text = message.notificationType
            itemView.findViewById<TextView>(R.id.tv_horse_name).text = message.horseName
            itemView.findViewById<TextView>(R.id.tv_notes).text = message.memo

            itemView.findViewById<ConstraintLayout>(R.id.cl_message).setOnClickListener {
                messageListListener.onView(bindingAdapterPosition,message)
            }

            itemView.findViewById<ImageButton>(R.id.btn_edit).setOnClickListener {
                messageListListener.onEdit(bindingAdapterPosition,message)
            }

            itemView.findViewById<ImageButton>(R.id.btn_trash).setOnClickListener {
                messageListListener.onDelete(bindingAdapterPosition,message)
            }
        }
    }

    interface MessageListListener {
        fun onView(position: Int, message: Message)
        fun onEdit(position: Int, message: Message)
        fun onDelete(position: Int, message: Message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(R.layout.item_message,parent,false),messageListListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messageList[position],position)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}