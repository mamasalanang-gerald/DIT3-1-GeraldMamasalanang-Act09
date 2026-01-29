package com.example.advanceduinavapp.repositories

import com.example.advanceduinavapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.tasks.await

class AuthRepository(private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()) {

    suspend fun login(email: String, password: String): Result<User> = try {
        val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        val user = authResult.user
        if (user != null) {
            Result.success(User(uid = user.uid, email = user.email ?: ""))
        } else {
            Result.failure(Exception("User is null after login"))
        }
    } catch (e: FirebaseAuthException) {
        Result.failure(Exception("Login failed: ${e.message}"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun logout(): Result<Unit> = try {
        firebaseAuth.signOut()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun getCurrentUser(): User? {
        val user = firebaseAuth.currentUser
        return if (user != null) {
            User(uid = user.uid, email = user.email ?: "")
        } else {
            null
        }
    }

    fun isUserAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }
}
