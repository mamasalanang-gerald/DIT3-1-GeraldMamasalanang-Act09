package com.example.advanceduinavapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.advanceduinavapp.models.ChatState
import com.example.advanceduinavapp.models.Message
import com.example.advanceduinavapp.models.User
import com.example.advanceduinavapp.repositories.AuthRepository
import com.example.advanceduinavapp.repositories.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ChatViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var chatRepository: ChatRepository

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var chatViewModel: ChatViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        whenever(chatRepository.getMessagesStream()).thenReturn(flowOf(emptyList()))
        chatViewModel = ChatViewModel(chatRepository, authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSendMessageRejectsEmptyContent() {
        chatViewModel.sendMessage("")
        
        val state = chatViewModel.chatState.value
        assertIs<ChatState.Error>(state)
        assertEquals("Message cannot be empty", (state as ChatState.Error).message)
    }

    @Test
    fun testSendMessageRejectsWhitespaceOnlyContent() {
        chatViewModel.sendMessage("   ")
        
        val state = chatViewModel.chatState.value
        assertIs<ChatState.Error>(state)
        assertEquals("Message cannot be empty", (state as ChatState.Error).message)
    }

    @Test
    fun testSendMessageFailsWhenUserNotAuthenticated() {
        whenever(authRepository.getCurrentUser()).thenReturn(null)
        
        chatViewModel.sendMessage("Hello")
        
        val state = chatViewModel.chatState.value
        assertIs<ChatState.Error>(state)
        assertEquals("User not authenticated", (state as ChatState.Error).message)
    }

    @Test
    fun testMessagesListIsEmptyInitially() {
        val messages = chatViewModel.messages.value
        assertEquals(emptyList(), messages)
    }

    @Test
    fun testMessagesAreSortedByTimestamp() {
        val message1 = Message(id = "1", senderEmail = "user1@example.com", content = "First", timestamp = 1000)
        val message2 = Message(id = "2", senderEmail = "user2@example.com", content = "Second", timestamp = 2000)
        val message3 = Message(id = "3", senderEmail = "user1@example.com", content = "Third", timestamp = 1500)
        
        val unsortedMessages = listOf(message1, message3, message2)
        val sortedMessages = unsortedMessages.sortedBy { it.timestamp }
        
        assertEquals(listOf(message1, message3, message2), sortedMessages)
    }
}
