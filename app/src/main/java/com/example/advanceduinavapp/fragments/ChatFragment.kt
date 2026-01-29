package com.example.advanceduinavapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.advanceduinavapp.R
import com.example.advanceduinavapp.adapters.MessageAdapter
import com.example.advanceduinavapp.models.ChatState
import com.example.advanceduinavapp.viewmodels.ChatViewModel

class ChatFragment : Fragment() {

    private val chatViewModel: ChatViewModel by viewModels()

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var logoutButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyMessageTextView: TextView
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageRecyclerView = view.findViewById(R.id.messageRecyclerView)
        messageInput = view.findViewById(R.id.messageInput)
        sendButton = view.findViewById(R.id.sendButton)
        logoutButton = view.findViewById(R.id.logoutButton)
        progressBar = view.findViewById(R.id.progressBar)
        emptyMessageTextView = view.findViewById(R.id.emptyMessageTextView)

        messageAdapter = MessageAdapter()
        messageRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
            adapter = messageAdapter
        }

        sendButton.setOnClickListener {
            val messageContent = messageInput.text.toString().trim()
            if (messageContent.isNotEmpty()) {
                chatViewModel.sendMessage(messageContent)
                messageInput.text.clear()
                messageInput.requestFocus()
            } else {
                Toast.makeText(requireContext(), "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        logoutButton.setOnClickListener {
            chatViewModel.logout()
            findNavController().navigate(R.id.action_chatFragment_to_authenticationFragment)
        }

        chatViewModel.messages.observe(viewLifecycleOwner) { messages ->
            messageAdapter.submitList(messages)
            if (messages.isNotEmpty()) {
                messageRecyclerView.scrollToPosition(messages.size - 1)
                emptyMessageTextView.visibility = View.GONE
            } else {
                emptyMessageTextView.visibility = View.VISIBLE
            }
        }

        chatViewModel.chatState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ChatState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                }
                is ChatState.Success -> {
                    progressBar.visibility = View.GONE
                }
                is ChatState.Error -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
