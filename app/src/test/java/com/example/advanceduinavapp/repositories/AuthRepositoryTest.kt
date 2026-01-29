package com.example.advanceduinavapp.repositories

import com.example.advanceduinavapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AuthRepositoryTest {

    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    @Mock
    private lateinit var firebaseUser: FirebaseUser

    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        authRepository = AuthRepository(firebaseAuth)
    }

    @Test
    fun testGetCurrentUserReturnsNullWhenNotAuthenticated() {
        whenever(firebaseAuth.currentUser).thenReturn(null)
        
        val result = authRepository.getCurrentUser()
        
        assertNull(result)
    }

    @Test
    fun testGetCurrentUserReturnsUserWhenAuthenticated() {
        whenever(firebaseAuth.currentUser).thenReturn(firebaseUser)
        whenever(firebaseUser.uid).thenReturn("test-uid")
        whenever(firebaseUser.email).thenReturn("test@example.com")
        
        val result = authRepository.getCurrentUser()
        
        assertEquals("test-uid", result?.uid)
        assertEquals("test@example.com", result?.email)
    }

    @Test
    fun testIsUserAuthenticatedReturnsFalseWhenNotAuthenticated() {
        whenever(firebaseAuth.currentUser).thenReturn(null)
        
        val result = authRepository.isUserAuthenticated()
        
        assertFalse(result)
    }

    @Test
    fun testIsUserAuthenticatedReturnsTrueWhenAuthenticated() {
        whenever(firebaseAuth.currentUser).thenReturn(firebaseUser)
        
        val result = authRepository.isUserAuthenticated()
        
        assertTrue(result)
    }
}
