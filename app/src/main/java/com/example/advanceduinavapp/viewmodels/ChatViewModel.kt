package com.example.advanceduinavapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.advanceduinavapp.models.ChatState
import com.example.advanceduinavapp.models.Message
import com.example.advanceduinavapp.repositories.AuthRepository
import com.example.advanceduinavapp.repositories.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatRepository: ChatRepository = ChatRepository(),
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _messages = MutableLiveData<List<Message>>(emptyList())
    val messages: LiveData<List<Message>> = _messages

    private val _chatState = MutableLiveData<ChatState>(ChatState.Success)
    val chatState: LiveData<ChatState> = _chatState

    init {
        loadMessages()
    }

    private fun loadMessages() {
        _chatState.value = ChatState.Loading
        viewModelScope.launch {
            chatRepository.getMessagesStream().collect { messageList ->
                _messages.value = messageList
                _chatState.value = ChatState.Success
            }
        }
    }

    fun sendMessage(content: String) {
        if (content.isBlank()) {
            _chatState.value = ChatState.Error("Message cannot be empty")
            return
        }

        val currentUser = authRepository.getCurrentUser()
        if (currentUser == null) {
            _chatState.value = ChatState.Error("User not authenticated")
            return
        }

        viewModelScope.launch {
            val message = Message(
                senderEmail = currentUser.email,
                content = content.trim(),
                timestamp = System.currentTimeMillis()
            )

            val result = chatRepository.sendMessage(message)
            result.fold(
                onSuccess = {
                    // Message sent successfully, UI will update via the listener
                },
                onFailure = { error ->
                    _chatState.value = ChatState.Error(error.message ?: "Failed to send message")
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
