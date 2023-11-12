package com.example.movieinfomation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.movieinfomation.R
import com.example.movieinfomation.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class LoginFragment: Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private var isSignIn = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Timber.tag("user").d("${currentUser.toString()}")
            auth.signOut()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickTextViewNotification()
        clickSignUpButton()
    }

    private fun clickSignUpButton() {
        binding.button.setOnClickListener {
            if (isSignIn) {
                signInWithAccount()
            } else {
                signUpWithEmail()
            }
        }

    }

    private fun signInWithAccount() {
        val email = binding.emailEt.text.toString()
        val password = binding.passET.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            showProgressBar()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {task ->
                    if (task.isSuccessful) {
                        hideProgressBar()
                        findNavController().navigate(R.id.action_loginFragment_to_splashFragment)
                    } else {
                        Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(requireContext(), "Email or Password is invalid!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signUpWithEmail() {
        val email = binding.emailEt.text.toString()
        val password = binding.passET.text.toString()
        val confirmPassword = binding.confirmPassET.text.toString()
        val name = binding.nameEt.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword && password.length > 6) {
            showProgressBar()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        hideProgressBar()
                        Firebase.auth.currentUser!!.updateProfile(userProfileChangeRequest {
                            displayName = name
                        })
                        Firebase.auth.signOut()
                        binding.textView.text = "Sign up successful, login to continue"
                        binding.textView.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(requireContext(), task.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(requireContext(), "Email or Password is invalid!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showProgressBar() {
        binding.loadingSignUp.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.loadingSignUp.visibility = View.GONE
    }

    private fun clickTextViewNotification() {
        binding.textView.setOnClickListener {
            if (isSignIn) {
                binding.apply {
                    textView.visibility = View.GONE
                    button.text = "SIGN UP"
                    confirmPasswordLayout.visibility = View.VISIBLE
                    binding.nameLayout.visibility = View.VISIBLE
                }
                isSignIn = false
            } else {
                binding.apply {
                    textView.text = "Not Registered Yet, Sign Up !"
                    textView.visibility = View.VISIBLE
                    button.text = "SIGN IN"
                    confirmPasswordLayout.visibility = View.GONE
                }
                isSignIn = true
            }
        }
    }
}