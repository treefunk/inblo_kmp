package com.colinjp.inblo.android.presentation.ui.horse_detail.dialogs

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.domain.util.fetchFile
import com.colinjp.inblo.android.presentation.ui.BaseDialogFragment
import com.colinjp.inblo.android.presentation.ui.horse_detail.tabs.AttachedFilesAdapter
import com.colinjp.inblo.domain.model.DailyAttachedFile
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

// TODO: use this for record daily dialog and record treatment dialog
abstract class FileAttachableFormFragment<VB: ViewBinding>: BaseDialogFragment() {

    lateinit var binding: VB

    abstract fun getViewBinding(): VB



    internal lateinit var attachedFilesAdapter: AttachedFilesAdapter
    internal var attachedFileList = ArrayList<DailyAttachedFile>()

    internal fun scanAttachedFiles(storageDir: File, uploadedFiles: List<DailyAttachedFile>? = null): ArrayList<File> {
        val dailyAttachedFiles = arrayListOf<DailyAttachedFile>()
        val files = arrayListOf<File>()
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
//            if(uploadedFiles == null){
//                storageDir.listFiles{ file ->
//                    file.delete()
//                    true
//                }
//                // clear images if there's any
//
//            }

        if(uploadedFiles != null){
            dailyAttachedFiles.addAll(uploadedFiles)
        }

        storageDir.listFiles { file ->
            dailyAttachedFiles.add(DailyAttachedFile(
                0,file.name,file.absolutePath,"image",null,null
            ))
            files.add(file)
            true
        }



        attachedFilesAdapter.attachedFileList = dailyAttachedFiles
        attachedFilesAdapter.notifyDataSetChanged()

        return files
    }

    internal fun attachedFileStorage(): File {
        return File(requireContext().filesDir, RecordDailyDialogFragment.ATTACHED_FILE_DIR)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RecordDailyDialogFragment.REQUEST_IMAGE_VIDEO_GET && resultCode == Activity.RESULT_OK ) {
            val thumbnail: Bitmap? = data?.getParcelableExtra("data")
            val fullPhotoUri: Uri? = data?.data

            if(fullPhotoUri != null){
                val file = fullPhotoUri.fetchFile(requireContext())
                if(file.exists()){
                    scanAttachedFiles(attachedFileStorage(),attachedFileList)
                }
            }
//            if(fullPhotoUri != null){
//                val photo = fullPhotoUri.toFile()
//                if(photo.exists()){
//                    Toast.makeText(requireContext(),"yes it exists",Toast.LENGTH_SHORT).show()
//                }
//            }
            // Do work with photo saved at fullPhotoUri

        }
    }

    internal fun selectImageOrVideo() {
        val choices = arrayOf("Image","Video")
        MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setTitle("Please select a file type:")
            .setItems(choices){ dialog, which ->
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                if(which == 0){
                    intent.type = "image/*"
                }else{
                    intent.type = "video/*"
                }

                if (intent.resolveActivity(requireContext().packageManager) != null) {
                    startActivityForResult(intent,
                        RecordDailyDialogFragment.REQUEST_IMAGE_VIDEO_GET
                    )
                }
            }.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = getViewBinding()

        attachedFilesAdapter = AttachedFilesAdapter(attachedFileList, object: AttachedFilesAdapter.DailyAttachedFileListener {
            override fun onView(position: Int, attachedFile: DailyAttachedFile) {
//                Toast.makeText(requireContext(),attachedFile.contentType,Toast.LENGTH_SHORT).show()
                val dialog = ViewAttachedFileDialogFragment.newInstance(arrayListOf(attachedFile),ViewAttachedFileDialogFragment.DIALOGTYPE.TREATMENT.dirString).also {
                    it.show(childFragmentManager,
                        RecordDailyDialogFragment.TAG_SHOW_ATTACH_FILE_DIALOG
                    )
                }
            }

            override fun onRemove(position: Int, attachedFile: DailyAttachedFile) {
                if(attachedFile.id == 0){
                    val file = File(attachedFile.filePath)
                    if(file.exists() && file.delete()){
                        Toast.makeText(requireContext(),"File removed successfully", Toast.LENGTH_SHORT).show()
                        scanAttachedFiles(attachedFileStorage(),attachedFileList)
                    }
                }
            }
        })


        attachedFileStorage().listFiles { f ->
            f.delete()
            true
        }

        val rvUploads = binding.root.findViewById<RecyclerView>(R.id.rv_uploads) // not sure how to get reference from the child fragments' views ???
       rvUploads.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.VERTICAL,false)
        rvUploads.adapter = attachedFilesAdapter


        binding.root.findViewById<Button>(R.id.btn_upload).setOnClickListener {
            selectImageOrVideo()
        }
        return binding.root
    }



}