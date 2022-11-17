package com.colinjp.inblo.android.presentation.ui.messages.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.databinding.DialogRecordDailyBinding
import com.colinjp.inblo.android.databinding.DialogSendMessageBinding
import com.colinjp.inblo.android.domain.util.initChoices
import com.colinjp.inblo.android.presentation.ui.BaseDialogFragment
import com.colinjp.inblo.android.presentation.ui.messages.MessagesViewModel
import com.colinjp.inblo.domain.model.Message
import com.colinjp.inblo.util.DataState
import kotlinx.coroutines.flow.collect
import timber.log.Timber


class SendMessageDialogFragment(): BaseDialogFragment() {

    private val messagesViewModel by activityViewModels<MessagesViewModel>()
    private var _binding: DialogSendMessageBinding? = null



    companion object {

        const val ARGS_SENDER_ID = "ARG_SENDER_ID"
        const val ARGS_STABLE_ID = "ARG_STABLE_ID"
        const val ARGS_MESSAGE = "ARG_MESSAGE"

        fun newInstance(senderId: Int, stableId: Int, message: Message? = null): SendMessageDialogFragment {
            val args = Bundle()
            args.putParcelable(ARGS_MESSAGE, message)
            args.putInt(ARGS_SENDER_ID,senderId)
            args.putInt(ARGS_STABLE_ID,stableId)
            val fragment = SendMessageDialogFragment()
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
        val binding = DialogSendMessageBinding.inflate(inflater,container,false)
        _binding = binding


        val message = requireArguments().getParcelable<Message>(ARGS_MESSAGE)
        val senderId = requireArguments().getInt(ARGS_SENDER_ID)
        val stableId = requireArguments().getInt(ARGS_STABLE_ID)



        if(message != null){
            if(message.horseId != null){
              binding.acHorseName.setTag(message.horseId.toString())
                binding.acHorseName.setText(message.horseName)
            }

            if(message.recipient != null){
                binding.tilUsername.visibility = View.VISIBLE
                binding.rgSendGroup.check(R.id.rb_username)
                binding.acUsername.setTag(message.recipientId.toString())
                binding.acUsername.setText(message.recipient?.username)
            }

            binding.etMessageTitle.setText(message.title)
            binding.acNotificationType.setText(message.notificationType,false)
            binding.etNotes.setText(message.memo)
        }


        binding.acNotificationType.initChoices(requireContext(), listOf("関係者連絡","状態関連","出走予定","調教相談","厩舎行事","その他"))

        binding.rgSendGroup.setOnCheckedChangeListener{ group, checkedId ->
            when(checkedId){
                R.id.rb_everyone -> {
                    binding.tilUsername.visibility = View.GONE
                    binding.acUsername.setText("")
                    binding.acUsername.setTag("0")
                }
                R.id.rb_username -> {
                    binding.tilUsername.visibility = View.VISIBLE
                    binding.acUsername.setText("")
                    binding.acUsername.setTag("")
                }
            }
        }

        binding.btnRecordCondition.setOnClickListener {

            val horse = if(binding.acHorseName.tag.toString().isNotBlank()) binding.acHorseName.tag.toString() else null
            val recipient = if(binding.acUsername.tag.toString().isNotBlank()) binding.acUsername.tag.toString() else null
            val title = binding.etMessageTitle.text.toString()
            val type = binding.acNotificationType.text.toString()
            val memo = binding.etNotes.text.toString()


            if(title.isBlank() || type.isBlank() || (binding.rgSendGroup.checkedRadioButtonId == R.id.rb_username && binding.acUsername.tag.toString().isBlank())){
                Toast.makeText(requireContext(),"すべての項目を入力してください。", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            messagesViewModel.sendMessage(
                stableId,
                senderId,
                recipient?.toInt(),
                horse?.toInt(),
                binding.acHorseName.text.toString(),
                type,
                title,
                memo,
                "0",
                message?.id
            )


            this.dismiss()
        }

        lifecycleScope.launchWhenCreated {
            messagesViewModel.users.collect { users ->
                when(users){
                    is DataState.Data -> {
                        binding.acUsername.initChoices(
                            requireContext(),
                            users.data.map { it.username.toString() }, { _,pos ->
                                if(pos != 0){
                                    binding.acUsername.tag = users.data[pos - 1].id.toString()
                                }else{
                                    binding.acUsername.tag = ""
                                }

                            }
                        )
                        Timber.v("users-> $users")
                    }
                    DataState.Empty -> { }
                    is DataState.Error -> {
                        Timber.e(users.exception)
                    }
                    is DataState.Loading -> { }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            messagesViewModel.horses.collect { horses ->
                when(horses){
                    is DataState.Data -> {
                        binding.acHorseName.initChoices(
                            requireContext(),
                            horses.data.map { it.name.toString() }, { _,pos ->
                                if(pos != 0){
                                    binding.acHorseName.tag = horses.data[pos - 1].id.toString()
                                }else{
                                    binding.acHorseName.tag = ""
                                }

                            }
                        )
                        Timber.v("horses-> $horses")
                    }
                    DataState.Empty -> { }
                    is DataState.Error -> {
                        Timber.e(horses.exception)
                    }
                    is DataState.Loading -> { }
                }
            }
        }



        binding.ibCloseDialog.setOnClickListener {
            this@SendMessageDialogFragment.dismiss()
        }

        return binding.root
    }



    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
