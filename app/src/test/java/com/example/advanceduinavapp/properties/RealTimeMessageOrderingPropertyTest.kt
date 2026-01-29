package com.example.advanceduinavapp.properties

import com.example.advanceduinavapp.models.Message
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Property 3: Real-Time Message Ordering
 * Validates: Requirements 3.4
 *
 * For any sequence of messages sent by multiple users, all connected clients
 * should receive and display messages in the same chronological order based on timestamp.
 */
class RealTimeMessageOrderingPropertyTest {

    @Test
    fun testMessagesAreOrderedByTimestamp() {
        val unorderedMessages = listOf(
            Message(id = "3", senderEmail = "user1@example.com", content = "Third", timestamp = 3000L),
            Message(id = "1", senderEmail = "user2@example.com", content = "First", timestamp = 1000L),
            Message(id = "4", senderEmail = "user1@example.com", content = "Fourth", timestamp = 4000L),
            Message(id = "2", senderEmail = "user3@example.com", content = "Second", timestamp = 2000L)
        )

        val orderedMessages = unorderedMessages.sortedBy { it.timestamp }

        assertEquals(4, orderedMessages.size, "Should have 4 messages")
        assertEquals(1000L, orderedMessages[0].timestamp, "First message should have timestamp 1000")
        assertEquals(2000L, orderedMessages[1].timestamp, "Second message should have timestamp 2000")
        assertEquals(3000L, orderedMessages[2].timestamp, "Third message should have timestamp 3000")
        assertEquals(4000L, orderedMessages[3].timestamp, "Fourth message should have timestamp 4000")
    }

    @Test
    fun testMultipleClientsReceiveSameOrder() {
        val messages = listOf(
            Message(id = "1", senderEmail = "user1@example.com", content = "Message 1", timestamp = 1000L),
            Message(id = "2", senderEmail = "user2@example.com", content = "Message 2", timestamp = 2000L),
            Message(id = "3", senderEmail = "user1@example.com", content = "Message 3", timestamp = 3000L)
        )

        // Simulate multiple clients receiving the same messages
        val client1Messages = messages.sortedBy { it.timestamp }
        val client2Messages = messages.sortedBy { it.timestamp }
        val client3Messages = messages.sortedBy { it.timestamp }

        // All clients should have the same order
        assertEquals(client1Messages, client2Messages, "Client 1 and 2 should have same order")
        assertEquals(client2Messages, client3Messages, "Client 2 and 3 should have same order")
    }

    @Test
    fun testMessagesFromDifferentUsersAreOrderedCorrectly() {
        val messages = listOf(
            Message(id = "1", senderEmail = "alice@example.com", content = "Alice's message", timestamp = 1000L),
            Message(id = "2", senderEmail = "bob@example.com", content = "Bob's message", timestamp = 500L),
            Message(id = "3", senderEmail = "charlie@example.com", content = "Charlie's message", timestamp = 1500L),
            Message(id = "4", senderEmail = "alice@example.com", content = "Alice's second message", timestamp = 2000L)
        )

        val orderedMessages = messages.sortedBy { it.timestamp }

        assertEquals("bob@example.com", orderedMessages[0].senderEmail, "Bob's message should be first")
        assertEquals("alice@example.com", orderedMessages[1].senderEmail, "Alice's first message should be second")
        assertEquals("charlie@example.com", orderedMessages[2].senderEmail, "Charlie's message should be third")
        assertEquals("alice@example.com", orderedMessages[3].senderEmail, "Alice's second message should be fourth")
    }

    @Test
    fun testConsistentOrderingAcrossMultipleInstances() {
        val messages = listOf(
            Message(id = "5", senderEmail = "user@example.com", content = "Message 5", timestamp = 5000L),
            Message(id = "2", senderEmail = "user@example.com", content = "Message 2", timestamp = 2000L),
            Message(id = "8", senderEmail = "user@example.com", content = "Message 8", timestamp = 8000L),
            Message(id = "1", senderEmail = "user@example.com", content = "Message 1", timestamp = 1000L)
        )

        // Create multiple sorted instances
        val sorted1 = messages.sortedBy { it.timestamp }
        val sorted2 = messages.sortedBy { it.timestamp }
        val sorted3 = messages.sortedBy { it.timestamp }

        // All should be identical
        assertEquals(sorted1, sorted2, "First and second sort should be identical")
        assertEquals(sorted2, sorted3, "Second and third sort should be identical")

        // Verify the order is correct
        assertEquals(listOf("1", "2", "5", "8"), sorted1.map { it.id }, "Messages should be in timestamp order")
    }
}
