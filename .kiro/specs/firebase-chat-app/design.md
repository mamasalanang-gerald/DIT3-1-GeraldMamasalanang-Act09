# Design Document: Firebase-Based Chat Application

## Overview

The Firebase Chat Application is a real-time messaging platform built for Android using Kotlin. It leverages Firebase Authentication for secure user login and Cloud Firestore for real-time message synchronization. The architecture follows a clean separation between UI, business logic, and data layers, enabling maintainability and testability.

## Architecture

The application follows a layered architecture:

```
┌─────────────────────────────────────────┐
│         UI Layer (Fragments)            │
│  - AuthenticationFragment               │
│  - ChatFragment                         │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      ViewModel & State Management       │
│  - AuthViewModel                        │
│  - ChatViewModel                        │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Repository Layer                   │
│  - AuthRepository                       │
│  - ChatRepository                       │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│      Firebase Services                  │
│  - Firebase Auth                        │
│  - Cloud Firestore                      │
└─────────────────────────────────────────┘
```

## Components and Interfaces

### 1. Authentication Layer

**AuthRepository**
- Handles Firebase Authentication operations
- Methods:
  - `login(email: String, password: String): Result<User>`
  - `logout(): Result<Unit>`
  - `getCurrentUser(): User?`
  - `isUserAuthenticated(): Boolean`

**AuthViewModel**
- Manages authentication state
- Exposes:
  - `authState: LiveData<AuthState>` (Loading, Success, Error, Unauthenticated)
  - `login(email: String, password: String)`
  - `logout()`

**AuthenticationFragment**
- UI for login/logout
- Displays email and password input fields
- Shows error messages
- Transitions to chat screen on successful authentication

### 2. Chat Layer

**Message Data Model**
```kotlin
data class Message(
    val id: String = "",
    val senderEmail: String = "",
    val content: String = "",
    val timestamp: Long = 0L
)
```

**ChatRepository**
- Handles Firestore operations for messages
- Methods:
  - `sendMessage(message: Message): Result<Unit>`
  - `getMessagesStream(): Flow<List<Message>>`
  - `deleteMessage(messageId: String): Result<Unit>`

**ChatViewModel**
- Manages chat state and message list
- Exposes:
  - `messages: LiveData<List<Message>>`
  - `chatState: LiveData<ChatState>` (Loading, Success, Error)
  - `sendMessage(content: String)`
  - `logout()`

**ChatFragment**
- Displays message list with RecyclerView
- Input field for composing messages
- Send button
- Auto-scrolls to latest message
- Shows loading indicator while fetching messages

### 3. Firebase Configuration

**FirebaseInitializer**
- Initializes Firebase on app startup
- Loads configuration from `google-services.json`
- Enables Firebase Auth and Firestore

## Data Models

### Message Document Structure (Firestore)

```
messages (collection)
├── {messageId} (document)
│   ├── senderEmail: String
│   ├── content: String
│   └── timestamp: Long (milliseconds since epoch)
```

### Authentication State

```kotlin
sealed class AuthState {
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
    object Unauthenticated : AuthState()
}
```

### Chat State

```kotlin
sealed class ChatState {
    object Loading : ChatState()
    object Success : ChatState()
    data class Error(val message: String) : ChatState()
}
```

## Correctness Properties

A property is a characteristic or behavior that should hold true across all valid executions of a system—essentially, a formal statement about what the system should do. Properties serve as the bridge between human-readable specifications and machine-verifiable correctness guarantees.

### Property 1: Authentication State Persistence

**For any** authenticated user, when the application is terminated and restarted, the user should remain authenticated without requiring re-login.

**Validates: Requirements 1.6**

### Property 2: Message Persistence Round-Trip

**For any** message sent by a user, when the message is persisted to Firestore and then retrieved, the sender email, content, and timestamp should match the original message exactly.

**Validates: Requirements 4.1, 4.2**

### Property 3: Real-Time Message Ordering

**For any** sequence of messages sent by multiple users, all connected clients should receive and display messages in the same chronological order based on timestamp.

**Validates: Requirements 3.4**

### Property 4: Unauthenticated Access Denial

**For any** unauthenticated user attempting to read or write messages, the Firestore security rules should deny the operation and return an authorization error.

**Validates: Requirements 6.1, 6.2**

### Property 5: Message Input Validation

**For any** message with empty or whitespace-only content, the system should reject the message and prevent it from being sent or stored.

**Validates: Requirements 2.2**

### Property 6: Real-Time Listener Activation

**For any** authenticated user viewing the chat screen, when a new message is added to Firestore by any user, the real-time listener should trigger within 5 seconds and update the local message list.

**Validates: Requirements 3.2, 3.3**

### Property 7: Session Restoration

**For any** user who was previously authenticated, when the application restarts, the authentication state should be restored from Firebase's cached session without requiring network access (if available).

**Validates: Requirements 1.6**

### Property 8: Error Message Display

**For any** authentication or Firestore operation that fails, the system should display a user-friendly error message to the user within 2 seconds.

**Validates: Requirements 7.1, 7.2**

## Error Handling

### Authentication Errors
- Invalid credentials → Display "Invalid email or password"
- Network error → Display "Network error. Please check your connection"
- Firebase initialization failure → Display "Unable to connect to Firebase"

### Firestore Errors
- Write failure → Display "Failed to send message. Tap to retry"
- Read failure → Display "Failed to load messages"
- Permission denied → Display "You don't have permission to perform this action"

### UI Error States
- All errors are displayed in a non-blocking toast or snackbar
- Errors include a retry option where applicable
- Errors are logged to Logcat for debugging

## Testing Strategy

### Unit Tests
- Test AuthRepository login/logout logic with mock Firebase Auth
- Test ChatRepository message operations with mock Firestore
- Test ViewModel state management and LiveData emissions
- Test Message data model serialization/deserialization
- Test input validation for message content

### Property-Based Tests
- **Property 1**: Verify authentication state persists across app restarts
- **Property 2**: Verify message round-trip (send → store → retrieve) preserves all fields
- **Property 3**: Verify message ordering is consistent across multiple clients
- **Property 4**: Verify Firestore rules deny unauthenticated access
- **Property 5**: Verify empty/whitespace messages are rejected
- **Property 6**: Verify real-time listener updates within acceptable time
- **Property 7**: Verify session restoration works correctly
- **Property 8**: Verify error messages are displayed for all failure scenarios

### Integration Tests
- Test full authentication flow (login → chat → logout)
- Test message sending and receiving end-to-end
- Test real-time synchronization with multiple simulated users
- Test offline message queueing and sync

### Test Configuration
- Minimum 100 iterations per property-based test
- Use Firebase Emulator Suite for local testing
- Mock Firebase services where appropriate for unit tests
- Use real Firebase for integration tests

