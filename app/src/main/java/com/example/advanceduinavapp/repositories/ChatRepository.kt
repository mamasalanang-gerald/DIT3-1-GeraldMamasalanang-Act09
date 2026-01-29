package com.example.advanceduinavapp.repositories

import com.example.advanceduinavapp.models.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepository(private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()) {

    suspend fun sendMessage(message: Message): Result<Unit> = try {
        if (message.content.isBlank()) {
            Result.failure(Exception("Message content cannot be empty"))
        } else {
            val messageData = mapOf(
                "senderEmail" to message.senderEmail,
                "content" to message.content.trim(),
                "timestamp" to System.currentTimeMillis()
            )
            
            firestore.collection("messages").add(messageData).await()
            Result.success(Unit)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun getMessagesStream(): Flow<List<Message>> = callbackFlow {
        val listener = firestore.collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val messages = snapshot.documents.mapNotNull { doc ->
                        try {
                            Message(
                                id = doc.id,
                                senderEmail = doc.getString("senderEmail") ?: "",
                                content = doc.getString("content") ?: "",
                                timestamp = doc.getLong("timestamp") ?: 0L
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    trySend(messages)
                }
            }

        awaitClose {
            listener.remove()
        }
    }

    suspend fun deleteMessage(messageId: String): Result<Unit> = try {
        firestore.collection("messages").document(messageId).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
