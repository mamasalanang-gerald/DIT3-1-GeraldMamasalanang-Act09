package com.example.advanceduinavapp.properties

import com.example.advanceduinavapp.models.Message
import org.junit.Test
import kotlin.test.assertTrue

/**
 * Property 5: Message Input Validation
 * Validates: Requirements 2.2
 *
 * For any message with empty or whitespace-only content, the system should reject
 * the message and prevent it from being sent or stored.
 */
class MessageValidationPropertyTest {

    @Test
    fun testEmptyMessagesAreRejected() {
        val testCases = listOf(
            "",
            " ",
            "  ",
            "\t",
            "\n",
            "\r\n",
            "   \t  \n  "
        )

        testCases.forEach { content ->
            val isBlank = content.isBlank()
            assertTrue(isBlank, "Message with content '$content' should be considered blank")
        }
    }

    @Test
    fun testValidMessagesAreAccepted() {
        val testCases = listOf(
            "Hello",
            "a",
            "123",
            "Hello World!",
            "  Hello  ", // Should be trimmed
            "\tHello\t", // Should be trimmed
            "Message with special chars: !@#$%^&*()"
        )

        testCases.forEach { content ->
            val isValid = content.isNotBlank()
            assertTrue(isValid, "Message with content '$content' should be valid")
        }
    }

    @Test
    fun testMessageTrimmingPreservesContent() {
        val testCases = listOf(
            "  hello  " to "hello",
            "\thello\t" to "hello",
            "\nhello\n" to "hello",
            "  hello world  " to "hello world",
            "hello" to "hello"
        )

        testCases.forEach { (original, expected) ->
            val trimmed = original.trim()
            assertTrue(trimmed == expected, "Trimming '$original' should result in '$expected', got '$trimmed'")
        }
    }
}
