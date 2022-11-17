package com.colinjp.inblo.android.presentation.ui.messages.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.colinjp.inblo.android.databinding.DialogViewMessageBinding
import com.colinjp.inblo.android.presentation.ui.BaseDialogFragment
import com.colinjp.inblo.domain.model.Message

class ViewMessageDialogFragment : BaseDialogFragment() {

    private var _binding: DialogViewMessageBinding? = null

    companion object {
        const val MESSAGE_ARG = "message_arg"
        fun newInstance(message: Message): ViewMessageDialogFragment {
            val args = Bundle()
            args.putParcelable(MESSAGE_ARG,message)
            val fragment = ViewMessageDialogFragment()
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
        val binding = DialogViewMessageBinding.inflate(inflater,container,false)
        _binding = binding

        val message = requireArguments().getParcelable<Message>(MESSAGE_ARG)!!

        binding.ibCloseDialog.setOnClickListener {
            this@ViewMessageDialogFragment.dismiss()
        }

        message.let {
            binding.labelTitle.text = message.title
            binding.labelSender.text = message.sender?.username
            binding.labelTime.text = message.formattedTime
            binding.labelHorseName.text = message.horseName
            binding.labelNotificationType.text = message.notificationType
            binding.labelMessageMemo.text = message.memo
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}