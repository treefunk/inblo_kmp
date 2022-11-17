package com.colinjp.inblo.android.presentation.ui.horse_detail.dialogs

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.databinding.DialogRecordTreatmentBinding
import com.colinjp.inblo.android.domain.util.fetchFile
import com.colinjp.inblo.android.domain.util.initChoices
import com.colinjp.inblo.android.domain.util.initDatePickerSpinner
import com.colinjp.inblo.android.presentation.ui.BaseDialogFragment
import com.colinjp.inblo.android.presentation.ui.horse_detail.HorseDetailViewModel
import com.colinjp.inblo.android.presentation.ui.horse_detail.tabs.AttachedFilesAdapter
import com.colinjp.inblo.domain.model.DailyAttachedFile
import com.colinjp.inblo.domain.model.HorseTreatment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class RecordTreatmentDialogFragment: BaseDialogFragment() {
    
    private val horseDetailViewModel by activityViewModels<HorseDetailViewModel>()
    private var _binding: DialogRecordTreatmentBinding? = null

    private lateinit var attachedFilesAdapter: AttachedFilesAdapter
    private var attachedFileList = ArrayList<DailyAttachedFile>()


    companion object {

        const val ARG_HORSE_ID = "ARG_HORSE_ID"
        const val ARG_HORSE_TREATMENT = "ARG_HORSE_TREATMENT"
        const val ATTACHED_FILE_DIR = "uploaded_treatment"
        const val TAG_SHOW_ATTACH_FILE_DIALOG = "SHOW_ATTACHED_FILE_DIALOG"



        fun newInstance(horseId: String, horseTreatment: HorseTreatment? = null): RecordTreatmentDialogFragment {
            val args = Bundle()
            args.putString(ARG_HORSE_ID,horseId)
            args.putParcelable(ARG_HORSE_TREATMENT, horseTreatment)
            val fragment = RecordTreatmentDialogFragment()
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
        val binding = DialogRecordTreatmentBinding.inflate(inflater,container,false)
        _binding = binding

        val horseId = requireArguments().getString(ARG_HORSE_ID)!!
        val horseTreatment = requireArguments().getParcelable<HorseTreatment>(ARG_HORSE_TREATMENT)

        attachedFilesAdapter = AttachedFilesAdapter(attachedFileList, object: AttachedFilesAdapter.DailyAttachedFileListener {
            override fun onView(position: Int, attachedFile: DailyAttachedFile) {
//                Toast.makeText(requireContext(),attachedFile.contentType,Toast.LENGTH_SHORT).show()
                if(attachedFile.id != 0){
                    val dialog = ViewAttachedFileDialogFragment.newInstance(arrayListOf(attachedFile),ViewAttachedFileDialogFragment.DIALOGTYPE.TREATMENT.dirString).also {
                        it.show(childFragmentManager,
                            RecordDailyDialogFragment.TAG_SHOW_ATTACH_FILE_DIALOG
                        )
                    }
                }
            }

            override fun onRemove(position: Int, attachedFile: DailyAttachedFile) {
                if(attachedFile.id == 0){
                    val file = File(attachedFile.filePath)
                    if(file.exists() && file.delete()){
                        Toast.makeText(requireContext(),"File removed successfully",Toast.LENGTH_SHORT).show()
                        scanAttachedFiles(attachedFileStorage(),attachedFileList)
                    }
                }
            }
        })

        attachedFileStorage().listFiles { f ->
            f.delete()
            true
        }


        if(horseTreatment != null){
            binding.etDate.setText(horseTreatment.date)
            binding.etTreatmentContent.setText(horseTreatment.treatmentDetail)
            binding.etVetName.setText(horseTreatment.doctorName)
            binding.etDrugName.setText(horseTreatment.medicineName)
            binding.etNotes.setText(horseTreatment.memo)
            binding.etInjuredPart.setText(horseTreatment.injuredPart)
            binding.acOccasionType.setText(horseTreatment.occasionType)
            scanAttachedFiles(attachedFileStorage(),horseTreatment.attachedFiles?.map { DailyAttachedFile.createFromDto(it) })
        }

        binding.acOccasionType.initChoices(requireContext(), listOf("故障・状態異常","獣医診察","治療","装蹄","飼い葉","その他"))

        binding.rvUploads.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.VERTICAL,false)
        binding.rvUploads.adapter = attachedFilesAdapter

        binding.etDate.initDatePickerSpinner(requireContext()){ binding.etTreatmentContent.requestFocus() }
        binding.ibCloseDialog.setOnClickListener { this@RecordTreatmentDialogFragment.dismiss() }


        binding.btnRecordCondition.setOnClickListener {
            val date = binding.etDate.text.toString()
            val treatmentDetail = binding.etTreatmentContent.text.toString()
            val injuredPart = binding.etInjuredPart.text.toString()
            val occasionType = binding.acOccasionType.text.toString()
            val vetName = binding.etVetName.text.toString()
            val drugName = binding.etDrugName.text.toString()
            val notes = binding.etNotes.text.toString()

            if(date.isBlank()){
                Toast.makeText(requireContext(),"日付を入力してください。", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(occasionType.isBlank()){
                Toast.makeText(requireContext(),"すべての項目を入力してください。", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dailyAttachedIds =  attachedFilesAdapter.attachedFileList.filter { it.id != 0 }.map { it.id.toString() }

            horseDetailViewModel.addHorseTreatment(
                horseId,
                date,
                vetName,
                treatmentDetail,
                injuredPart,
                occasionType,
                drugName,
                notes,
                scanAttachedFiles(attachedFileStorage(),attachedFilesAdapter.attachedFileList),
                dailyAttachedIds,
                horseTreatment?.id?.toString()
            )

            this.dismiss()
        }

        binding.btnUpload.setOnClickListener {
            selectImageOrVideo()
        }

        return binding.root
    }

    private fun selectImageOrVideo() {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RecordDailyDialogFragment.REQUEST_IMAGE_VIDEO_GET && resultCode == Activity.RESULT_OK ) {
            val thumbnail: Bitmap? = data?.getParcelableExtra("data")
            val fullPhotoUri: Uri? = data?.data

            if(fullPhotoUri != null){
                val file = fullPhotoUri.fetchFile(requireContext())
                if(file.exists()){
                    scanAttachedFiles(attachedFileStorage(),attachedFilesAdapter.attachedFileList)
                }
            }

        }
    }

    private fun scanAttachedFiles(storageDir: File, uploadedFiles: List<DailyAttachedFile>? = null): ArrayList<File> {
        val dailyAttachedFiles = arrayListOf<DailyAttachedFile>()
        val files = arrayListOf<File>()
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        dailyAttachedFiles.clear()
        if(uploadedFiles != null){
            dailyAttachedFiles.addAll(uploadedFiles.filter { it.id != 0 })
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

    private fun attachedFileStorage(): File {
        return File(requireContext().filesDir, RecordDailyDialogFragment.ATTACHED_FILE_DIR)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}