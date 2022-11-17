package com.colinjp.inblo.android.presentation.ui.horse_detail.tabs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.domain.util.displayBlankIfZero
import com.colinjp.inblo.domain.model.DailyAttachedFile
import com.colinjp.inblo.domain.model.HorseDaily
import java.io.File

class AttachedFilesAdapter(
    var attachedFileList: ArrayList<DailyAttachedFile>,
    private val attachedFileListener: DailyAttachedFileListener
): RecyclerView.Adapter<AttachedFilesAdapter.ViewHolder>() {

    inner class ViewHolder(
        itemView: View,
        attachedFileListener: DailyAttachedFileListener
    ) : RecyclerView.ViewHolder(itemView) {
        init {

        }

        fun bind(attachedFile: DailyAttachedFile) {
            itemView.findViewById<TextView>(R.id.tv_file_name).apply {
                text = attachedFile.name
                setOnClickListener {
                    attachedFileListener.onView(bindingAdapterPosition,attachedFile)
                }
            }
            itemView.findViewById<ImageButton>(R.id.ib_remove).setOnClickListener {
                attachedFileList.removeAt(bindingAdapterPosition)
                notifyItemRemoved(bindingAdapterPosition)
                attachedFileListener.onRemove(bindingAdapterPosition,attachedFile)
            }
        }
    }

    interface DailyAttachedFileListener {
        fun onView(position: Int, attachedFile: DailyAttachedFile)
        fun onRemove(position: Int, attachedFile: DailyAttachedFile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(R.layout.item_upload, parent, false), attachedFileListener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(attachedFileList[position])
    }

    override fun getItemCount(): Int {
        return attachedFileList.size
    }
}