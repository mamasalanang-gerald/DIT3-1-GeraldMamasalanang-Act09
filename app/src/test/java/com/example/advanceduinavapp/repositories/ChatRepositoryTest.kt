package com.example.advanceduinavapp.repositories

import com.example.advanceduinavapp.models.Message
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChatRepositoryTest {

    @Test
    fun testSendMessageRejectsEmptyContent() = runBlocking {
        val chatRepository = ChatRepository()
        val message = Message(
            senderEmail = "test@example.com",
            content = "",
            timestamp = System.currentTimeMillis()
        )

        val result = chatRepository.sendMessage(message)

        assertTrue(result.isFailure)
        assertEquals("Message content cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun testSendMessageRejectsWhitespaceOnlyContent() = runBlocking {
        val chatRepository = ChatRepository()
        val message = Message(
            senderEmail = "test@example.com",
            content = "   ",
            timestamp = System.currentTimeMillis()
        )

        val result = chatRepository.sendMessage(message)

        assertTrue(result.isFailure)
        assertEquals("Message content cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun testMessageTrimsWhitespace() {
        val message = Message(
            senderEmail = "test@example.com",
            content = "  hello world  ",
            timestamp = System.currentTimeMillis()
        )

        val trimmedContent = message.content.trim()

        assertEquals("hello world", trimmedContent)
    }
}
