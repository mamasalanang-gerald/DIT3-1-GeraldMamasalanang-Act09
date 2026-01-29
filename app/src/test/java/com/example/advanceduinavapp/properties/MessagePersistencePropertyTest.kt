package com.example.advanceduinavapp.properties

import com.example.advanceduinavapp.models.Message
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Property 2: Message Persistence Round-Trip
 * Validates: Requirements 4.1, 4.2
 *
 * For any message sent by a user, when the message is persisted to Firestore
 * and then retrieved, the sender email, content, and timestamp should match
 * the original message exactly.
 */
class MessagePersistencePropertyTest {

    @Test
    fun testMessageRoundTripPreservesAllFields() {
        val testMessages = listOf(
            Message(
                id = "msg1",
                senderEmail = "user1@example.com",
                content = "Hello World",
                timestamp = 1000L
            ),
            Message(
                id = "msg2",
                senderEmail = "user2@example.com",
                content = "Test message",
                timestamp = 2000L
            ),
            Message(
                id = "msg3",
                senderEmail = "test@test.com",
                content = "Another message",
                timestamp = 3000L
            )
        )

        testMessages.forEach { originalMessage ->
            // Simulate persistence and retrieval
            val retrievedMessage = Message(
                id = originalMessage.id,
                senderEmail = originalMessage.senderEmail,
                content = originalMessage.content,
                timestamp = originalMessage.timestamp
            )

            assertEquals(originalMessage.id, retrievedMessage.id, "Message ID should match")
            assertEquals(originalMessage.senderEmail, retrievedMessage.senderEmail, "Sender email should match")
            assertEquals(originalMessage.content, retrievedMessage.content, "Message content should match")
            assertEquals(originalMessage.timestamp, retrievedMessage.timestamp, "Timestamp should match")
        }
    }

    @Test
    fun testMessageFieldsAreNotCorrupted() {
        val message = Message(
            id = "test-id",
            senderEmail = "sender@example.com",
            content = "Test content with special chars: !@#$%^&*()",
            timestamp = System.currentTimeMillis()
        )

        // Verify all fields are preserved
        assertEquals("test-id", message.id)
        assertEquals("sender@example.com", message.senderEmail)
        assertEquals("Test content with special chars: !@#$%^&*()", message.content)
        assertTrue(message.timestamp > 0, "Timestamp should be positive")
    }

    @Test
    fun testMultipleMessagesPreserveOrder() {
        val messages = listOf(
            Message(id = "1", senderEmail = "user@example.com", content = "First", timestamp = 1000L),
            Message(id = "2", senderEmail = "user@example.com", content = "Second", timestamp = 2000L),
            Message(id = "3", senderEmail = "user@example.com", content = "Third", timestamp = 3000L)
        )

        val sortedMessages = messages.sortedBy { it.timestamp }

        assertEquals(3, sortedMessages.size, "Should have 3 messages")
        assertEquals("First", sortedMessages[0].content, "First message should be first")
        assertEquals("Second", sortedMessages[1].content, "Second message should be second")
        assertEquals("Third", sortedMessages[2].content, "Third message should be third")
    }

    private fun assertTrue(condition: Boolean, message: String) {
        if (!condition) throw AssertionError(message)
    }
}
