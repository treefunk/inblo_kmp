package com.colinjp.inblo.android.presentation.ui.messages

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.databinding.FragmentMessagesBinding
import com.colinjp.inblo.android.domain.util.UserPreferences
import com.colinjp.inblo.android.presentation.ui.horse_list.SortOrder
import com.colinjp.inblo.android.presentation.ui.messages.dialogs.SendMessageDialogFragment
import com.colinjp.inblo.android.presentation.ui.messages.dialogs.ViewMessageDialogFragment
import com.colinjp.inblo.domain.model.Message
import com.colinjp.inblo.util.DataState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MessagesFragment: Fragment(R.layout.fragment_messages){

    private var messagesBinding: FragmentMessagesBinding? = null
    private val messagesViewModel by activityViewModels<MessagesViewModel>()
    private lateinit var adapter: MessageListAdapter

    @Inject
    lateinit var userPreferencesFlow: Flow<UserPreferences>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMessagesBinding.bind(view)
        messagesBinding = binding

        lifecycleScope.launchWhenCreated {
            userPreferencesFlow.collect { up ->
                adapter = MessageListAdapter(
                    listOf(), up.userId, object : MessageListAdapter.MessageListListener {
                        override fun onView(position: Int, message: Message) {
                            ViewMessageDialogFragment.newInstance(message)
                                .show(childFragmentManager, "SHOW_MESSAGE_DIALOG")
                        }

                        override fun onEdit(position: Int, message: Message) {

                            val dialog = SendMessageDialogFragment.newInstance(
                                up.userId,
                                up.stableId,
                                message
                            )
                            dialog.show(childFragmentManager, "TAG_SEND_MESSAGE")
                        }

                        override fun onDelete(position: Int, message: Message) {
                            MaterialAlertDialogBuilder(
                                requireContext(),
                                R.style.ThemeOverlay_App_MaterialAlertDialog
                            )
                                .setNeutralButton("Cancel", null)
                                .setNegativeButton("DELETE") { dialog, which ->
                                    if (which == DialogInterface.BUTTON_NEGATIVE) {
                                        messagesViewModel.deleteMessage(message.id)
                                    }
                                }
                                .setTitle("Warning!")
                                .setMessage("Are you sure you want to delete this item?")
                                .setIcon(
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.ic_baseline_priority_high_24
                                    )
                                ).show()
                        }

                    }
                )

                binding.rvMessages.adapter = adapter
                binding.rvMessages.layoutManager =
                    LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            }
        }
        lifecycleScope.launchWhenCreated {
            messagesViewModel.messages
                .collect {
                    when(it){
                        is DataState.Data -> {
                            val messagelist = it.data
                            adapter.messageList = messagelist
                            if(messagelist.size > 0){
                                binding.flowColumns.visibility = View.VISIBLE
                            }else{
                                binding.flowColumns.visibility = View.INVISIBLE
                            }
                            adapter.notifyDataSetChanged()
                            Timber.v(it.toString())
                        }
                        DataState.Empty -> {

                        }
                        is DataState.Error -> {
                            Timber.e(it.toString())
                        }
                        is DataState.Loading -> {

                        }
                    }
                }
        }


        binding.horScrollRecycler.viewTreeObserver.addOnScrollChangedListener {
            val scrollX = binding.horScrollRecycler.scrollX
            binding.horScrollLabels.post {
                binding.horScrollLabels.scrollTo(scrollX,0)
            }
        }

        lifecycleScope.launchWhenCreated {
            userPreferencesFlow.collect {
                messagesViewModel.getMessages(it.userId)
            }
        }

        lifecycleScope.launchWhenCreated {
            userPreferencesFlow.collect { up ->
                messagesViewModel.getHorses(SortOrder.BY_DATE,up.stableId)

                binding.btnComposeMessage.setOnClickListener {
                    val dialog = SendMessageDialogFragment.newInstance(up.userId,up.stableId)
                    dialog.show(childFragmentManager,"TAG_SEND_MESSAGE")
                }

                messagesViewModel.getUsernames(stableId = up.stableId.toString(), excludeId = up.userId.toString())
            }
        }

        lifecycleScope.launchWhenCreated {
            messagesViewModel.eventFlow.collect { event ->
                when(event){
                    is MessagesViewModel.MessageEvent.Error -> {
                        Toast.makeText(requireContext(),"通信に問題があります。後ほどアクセスしてください。",Toast.LENGTH_SHORT).show()
                    }
                    is MessagesViewModel.MessageEvent.Loading -> {

                    }
                    is MessagesViewModel.MessageEvent.Success -> {
                        Toast.makeText(requireContext(),event.message,Toast.LENGTH_SHORT).show()
                        lifecycleScope.launchWhenCreated {
                            userPreferencesFlow.collect {
                                messagesViewModel.getMessages(it.userId)
                            }
                        }
                    }
                }
            }
        }




    }

    override fun onDestroyView() {
        messagesBinding = null
        super.onDestroyView()
    }
}