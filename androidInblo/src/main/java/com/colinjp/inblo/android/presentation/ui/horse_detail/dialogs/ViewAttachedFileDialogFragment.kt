package com.colinjp.inblo.android.presentation.ui.horse_detail.dialogs

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.colinjp.inblo.android.databinding.DialogViewAttachedFileBinding
import com.colinjp.inblo.android.di.BaseURL
import com.colinjp.inblo.android.presentation.ui.BaseDialogFragment
import com.colinjp.inblo.domain.model.DailyAttachedFile
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ViewAttachedFileDialogFragment(): BaseDialogFragment() {

    private var _binding: DialogViewAttachedFileBinding? = null
    private var currentIndex = 0

    @Inject
    @BaseURL
    lateinit var baseUrl: String

    companion object {

        const val ARGS_DAILY_ATTACHED_FILE = "ARGS_DAILY_ATTACHED_FILE"
        const val ARGS_ATTACH_FILE_DIR = "ARGS_ATTACH_FILE_DIR"


        fun newInstance(dailyAttachedFileList: ArrayList<DailyAttachedFile>, dirString: String): ViewAttachedFileDialogFragment  {
            val args = Bundle()
            args.putString(ARGS_ATTACH_FILE_DIR, dirString)
            args.putParcelableArrayList(ARGS_DAILY_ATTACHED_FILE, dailyAttachedFileList)
            val fragment = ViewAttachedFileDialogFragment()
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
        val binding = DialogViewAttachedFileBinding.inflate(inflater,container,false)
        _binding = binding

        val dailyAttachedFileList = requireArguments().getParcelableArrayList<DailyAttachedFile>(ARGS_DAILY_ATTACHED_FILE)!!


        initMediaItem(binding, dailyAttachedFileList, currentIndex)

        binding.ibPrevMedia.setOnClickListener {
            initMediaItem(binding, dailyAttachedFileList, if(currentIndex > 0 ) --currentIndex else 0)
        }

        binding.ibNextMedia.setOnClickListener {
            initMediaItem(binding,dailyAttachedFileList,if(currentIndex < dailyAttachedFileList.size) ++currentIndex else currentIndex)
        }


        binding.ibCloseDialog.setOnClickListener {
            this@ViewAttachedFileDialogFragment.dismiss()
        }

        return binding.root
    }

    private fun initMediaItem(binding: DialogViewAttachedFileBinding, dailyAttachedFileList: ArrayList<DailyAttachedFile>, index: Int){

        val attachedFileDir = requireArguments().getString(ARGS_ATTACH_FILE_DIR)!!

        if(dailyAttachedFileList.size == 1){
            binding.ibPrevMedia.visibility = View.GONE
            binding.ibNextMedia.visibility = View.GONE
        }

        binding.ibPrevMedia.visibility = if(index != 0) View.VISIBLE else View.INVISIBLE
        binding.ibNextMedia.visibility = if(index < (dailyAttachedFileList.size - 1)) View.VISIBLE else View.INVISIBLE


        val dailyAttachedFile = dailyAttachedFileList[index]
        val url = baseUrl + attachedFileDir + dailyAttachedFile.filePath
        Timber.v("url: ${url}")

        val isImage = dailyAttachedFile.contentType.startsWith("image/")


        binding.pvPlayer.visibility = if(isImage) View.GONE else View.VISIBLE
        binding.ivImage.visibility = if(isImage) View.VISIBLE else View.GONE

        if(isImage){
            Glide.with(requireContext())
                .load(url)
                .fitCenter()
                .into(binding.ivImage)
        }else{
            val player = SimpleExoPlayer.Builder(requireContext()).build()
            val mediaItem = MediaItem.Builder()
                .setUri(url)
                .build()
            player.addMediaItem(mediaItem)
            binding.pvPlayer.player = player
            player.prepare()
            player.play()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    enum class DIALOGTYPE(val dirString: String) {
        DAILY("/daily-reports-file/"),
        TREATMENT("/treatments-file/")
    }
}