package com.example.advanceduinavapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.advanceduinavapp.R
import com.example.advanceduinavapp.models.AuthState
import com.example.advanceduinavapp.viewmodels.AuthViewModel

class AuthenticationFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_authentication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        loginButton = view.findViewById(R.id.loginButton)
        progressBar = view.findViewById(R.id.progressBar)
        errorTextView = view.findViewById(R.id.errorTextView)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                errorTextView.text = "Email and password cannot be empty"
                errorTextView.visibility = View.VISIBLE
                return@setOnClickListener
            }

            authViewModel.login(email, password)
        }

        authViewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthState.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    loginButton.isEnabled = false
                    errorTextView.visibility = View.GONE
                }
                is AuthState.Success -> {
                    progressBar.visibility = View.GONE
                    loginButton.isEnabled = true
                    findNavController().navigate(R.id.action_authenticationFragment_to_chatFragment)
                }
                is AuthState.Error -> {
                    progressBar.visibility = View.GONE
                    loginButton.isEnabled = true
                    errorTextView.text = state.message
                    errorTextView.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
                is AuthState.Unauthenticated -> {
                    progressBar.visibility = View.GONE
                    loginButton.isEnabled = true
                }
            }
        }
    }
}
