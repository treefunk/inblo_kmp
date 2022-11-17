package com.colinjp.inblo.android.presentation.ui.horse_detail.dialogs

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.databinding.DialogRecordDailyBinding
import com.colinjp.inblo.android.di.BaseURL
import com.colinjp.inblo.android.domain.util.fetchFile
import com.colinjp.inblo.android.domain.util.ifBlankZero
import com.colinjp.inblo.android.domain.util.initChoices
import com.colinjp.inblo.android.domain.util.initDatePickerSpinner
import com.colinjp.inblo.android.presentation.ui.BaseDialogFragment
import com.colinjp.inblo.android.presentation.ui.horse_detail.HorseDetailViewModel
import com.colinjp.inblo.android.presentation.ui.horse_detail.tabs.AttachedFilesAdapter
import com.colinjp.inblo.domain.model.DailyAttachedFile
import com.colinjp.inblo.domain.model.HorseDaily
import com.colinjp.inblo.util.DataState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class RecordDailyDialogFragment(): BaseDialogFragment() {

    private val horseDetailViewModel by activityViewModels<HorseDetailViewModel>()
    private var _binding: DialogRecordDailyBinding? = null
    private lateinit var attachedFilesAdapter: AttachedFilesAdapter
    private var attachedFileList = ArrayList<DailyAttachedFile>()



    companion object {
        const val REQUEST_IMAGE_VIDEO_GET = 1

        const val ARG_HORSE_ID = "ARG_HORSE_ID"
        const val ARGS_HORSE_DAILY = "ARG_HORSE_DAILY"
        const val ATTACHED_FILE_DIR = "uploaded_daily"
        const val TAG_SHOW_ATTACH_FILE_DIALOG = "SHOW_ATTACHED_FILE_DIALOG"

        fun newInstance(horseId: String, horseDaily: HorseDaily? = null): RecordDailyDialogFragment {
            val args = Bundle()
            args.putString(ARG_HORSE_ID,horseId)
            args.putParcelable(ARGS_HORSE_DAILY, horseDaily)
            val fragment = RecordDailyDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
//        val view = inflater.inflate(R.layout.dialog_record_daily.xml,container,false)
        val binding = DialogRecordDailyBinding.inflate(inflater,container,false)
        _binding = binding

        val horseId = requireArguments().getString(ARG_HORSE_ID)!!
        val horseDaily = requireArguments().getParcelable<HorseDaily>(ARGS_HORSE_DAILY)


        attachedFilesAdapter = AttachedFilesAdapter(attachedFileList, object: AttachedFilesAdapter.DailyAttachedFileListener {
            override fun onView(position: Int, attachedFile: DailyAttachedFile) {
//                Toast.makeText(requireContext(),attachedFile.contentType,Toast.LENGTH_SHORT).show()
                if(attachedFile.id != 0){
                    val dialog = ViewAttachedFileDialogFragment.newInstance(arrayListOf(attachedFile),ViewAttachedFileDialogFragment.DIALOGTYPE.DAILY.dirString).also {
                        it.show(childFragmentManager, TAG_SHOW_ATTACH_FILE_DIALOG)
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

        if(horseDaily != null){
            binding.etDate.setText(horseDaily.date)
            binding.etBodyTemp.setText(horseDaily.bodyTemperature.toString())
            binding.etWeight.setText(horseDaily.horseWeight.toString())
            binding.acConditionGroup.setText(horseDaily.conditionGroup)

//            binding.acRiderName.setText(horseDaily.riderName)
//            binding.acTrainingType.setText(horseDaily.trainingTypeName)

            binding.acRiderName.apply {
                setText(horseDaily.rider?.name)
                if(horseDaily.rider?.id != null){
                    tag = horseDaily.rider?.id.toString()
                }else{
                    tag = "0"
                }
            }

            binding.acTrainingType.apply {
                setText(horseDaily.trainingType?.name)
                if(horseDaily.trainingType?.id != null){
                    tag = horseDaily.trainingType?.id.toString()
                }else{
                    tag = "0"
                }
            }


//            binding.acRiderName.apply {
//                setText(horse.)
//            }
            binding.acTrainingAmount.setText(horseDaily.trainingAmount)
            binding.et3f.setText(horseDaily.time3f.toString())
            binding.et4f.setText(horseDaily.time4f.toString())
            binding.et5f.setText(horseDaily.time5f.toString())
            binding.etNotes.setText(horseDaily.memo)
            scanAttachedFiles(attachedFileStorage(),horseDaily.attachedFiles?.map { DailyAttachedFile.createFromDto(it) })
        }






        binding.rvUploads.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        binding.rvUploads.adapter = attachedFilesAdapter


        binding.etBodyTemp.setOnClickListener { etBodyTemp ->

            val inf = LayoutInflater.from(requireContext())
            val v = inf.inflate(R.layout.dialog_temperature_picker,null)

            val firstNum = v.findViewById<NumberPicker>(R.id.np_temp).apply {
                minValue = 35
                maxValue = 42
            }

            val secondNum = v.findViewById<NumberPicker>(R.id.np_temp_decimal).apply {
                minValue = 0
                maxValue = 9
            }

            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setView(v)
                .setNegativeButton("Cancel",null)
                .setPositiveButton("OK") { dialog, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        val sb = StringBuilder()
                        sb.append(firstNum.value)
                        sb.append(".")
                        sb.append(secondNum.value)
                        (etBodyTemp as TextInputEditText).setText(sb.toString())
                        binding.etWeight.requestFocus()
                    }
                }.create()


            dialog.show()

//            dialog.fin
        }


        binding.acTrainingAmount.setOnClickListener { acTrainingAmount ->

            val inf = LayoutInflater.from(requireContext())
            val v = inf.inflate(R.layout.dialog_amount_picker,null)



//            Timber.v("i is = ${values.toTypedArray().size}")
            var i = 0
            val maxV = 8000
            val values = arrayListOf<String>()
            values.clear()
            while (i <= maxV) {
                values.add(i.toString())
                i += 200
            }


            val firstNum = v.findViewById<NumberPicker>(R.id.np_temp).apply {
                displayedValues = null
                minValue = 1
                maxValue = values.size
                displayedValues = values.toTypedArray()
            }


            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setView(v)
                .setNegativeButton("Cancel",null)
                .setPositiveButton("OK") { dialog, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {{
                        val sb = StringBuilder()
                        sb.append(values[firstNum.value - 1])
                        (acTrainingAmount as MaterialAutoCompleteTextView).setText(sb.toString())
                        binding.etWeight.requestFocus()
                    }
                        val sb = StringBuilder()
                        sb.append(values[firstNum.value - 1])
                        (acTrainingAmount as MaterialAutoCompleteTextView).setText(sb.toString())
                        binding.etWeight.requestFocus()
                    }
                }.create()


            dialog.show()

//            dialog.fin
        }



        binding.acConditionGroup.initChoices(requireContext(), listOf("良","稍重","重","不良"))

        lifecycleScope.launchWhenCreated {
            horseDetailViewModel.dailyReportFormData.collect { response ->
                when(response){
                    is DataState.Data -> {
                        val data = response.data.data
                        Timber.v("dailyreportform: $data")

//                        binding.acRiderName.initChoices(requireContext(), data.riders.map { it.name })
                        binding.acRiderName.initChoices(
                            requireContext(),
                            data.riders.map { it.name.toString() }, { _,pos ->
                                if(pos != 0){
                                    binding.acRiderName.tag = data.riders[pos - 1].id.toString()
                                }else{
                                    binding.acRiderName.tag = ""
                                }

                            }
                        )

//                        binding.acTrainingType.initChoices(requireContext(), data.trainingTypes.map { it.name })
                        binding.acTrainingType.initChoices(
                            requireContext(),
                            data.trainingTypes.map { it.name.toString() }, { _,pos ->
                                if(pos != 0){
                                    binding.acTrainingType.tag = data.trainingTypes[pos - 1].id.toString()
                                }else{
                                    binding.acTrainingType.tag = ""
                                }

                            }
                        )
                    }
                    DataState.Empty -> {

                    }
                    is DataState.Error -> {
                        Timber.e(response.exception)
                    }
                    is DataState.Loading -> { }
                }
            }
        }

//        binding.acRiderName.initChoices(requireContext(), listOf("西","後藤","望月","横山","安藤","他騎手","その他"))
//        binding.acTrainingType.initChoices(requireContext(), listOf("本馬場","角馬場","内馬場","角馬場→内馬場","角馬場→本馬場","内馬場→本馬場","角馬場→内馬場→本馬場","中間追切","最終追切","出走日","全休日"))

        binding.etDate.initDatePickerSpinner(requireContext())
        binding.ibCloseDialog.setOnClickListener { this@RecordDailyDialogFragment.dismiss() }
        binding.btnRecordCondition.setOnClickListener {
            val date = binding.etDate.text.toString()
            val bodyTemperature = binding.etBodyTemp.text.toString().ifBlankZero()
            val horseWeight = binding.etWeight.text.toString().ifBlankZero()
            val conditionGroup = binding.acConditionGroup.text.toString()
            val rider = if(binding.acRiderName.tag.toString().isNotBlank()) binding.acRiderName.tag.toString() else null
            val trainingType = if(binding.acTrainingType.tag.toString().isNotBlank()) binding.acTrainingType.tag.toString() else null
            val trainingAmount = binding.acTrainingAmount.text.toString()
            val time5f = binding.et5f.text.toString().ifBlankZero()
            val time4f = binding.et4f.text.toString().ifBlankZero()
            val time3f = binding.et3f.text.toString().ifBlankZero()
            val notes = binding.etNotes.text.toString()

            if(date.isBlank()){
                Toast.makeText(requireContext(),"日付を入力してください。",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dailyAttachedIds =  attachedFilesAdapter.attachedFileList.filter { it.id != 0 }.map { it.id.toString() }
//            Toast.makeText(requireContext(),dailyAttachedIds.joinToString(","),Toast.LENGTH_SHORT).show()


                horseDetailViewModel.addHorseDaily(
                    horseId,
                    date,
                    if (bodyTemperature.isNotBlank()) bodyTemperature else "0.0",
                    if (horseWeight.isNotBlank()) horseWeight else "0.0",
                    conditionGroup,
                    "",
                    "",
                    rider?.toInt(),
                    trainingType?.toInt(),
                    trainingAmount,
                    time5f,
                    time4f,
                    time3f,
                    notes,
                    scanAttachedFiles(attachedFileStorage(),attachedFilesAdapter.attachedFileList),
                    dailyAttachedIds,
                    horseDaily?.id?.toString()
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
        MaterialAlertDialogBuilder(requireContext(),R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setTitle("Please select a file type:")
            .setItems(choices){ dialog, which ->
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                if(which == 0){
                    intent.type = "image/*"
                }else{
                    intent.type = "video/*"
                }

                if (intent.resolveActivity(requireContext().packageManager) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_VIDEO_GET)
                }
            }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_VIDEO_GET && resultCode == Activity.RESULT_OK ) {
            val thumbnail: Bitmap? = data?.getParcelableExtra("data")
            val fullPhotoUri: Uri? = data?.data

            if(fullPhotoUri != null){
                val file = fullPhotoUri.fetchFile(requireContext())
                if(file.exists()){
                    scanAttachedFiles(attachedFileStorage(),attachedFilesAdapter.attachedFileList)
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

    private fun scanAttachedFiles(storageDir: File, uploadedFiles: List<DailyAttachedFile>? = null): ArrayList<File> {
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
        return File(requireContext().filesDir, ATTACHED_FILE_DIR)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}


