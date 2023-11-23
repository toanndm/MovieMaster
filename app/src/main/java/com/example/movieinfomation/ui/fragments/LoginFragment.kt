package com.example.movieinfomation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.movieinfomation.R
import com.example.movieinfomation.databinding.FragmentLoginBinding
import com.example.movieinfomation.other.AppUtils
import com.example.movieinfomation.other.NetWorkResult
import com.example.movieinfomation.ui.viewmodels.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class LoginFragment: Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var countLoad = 0
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
            Timber.tag("user").d("$currentUser")
            auth.signOut()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickTextViewNotification()
        clickSignUpButton()
        binding.layoutContainer.setOnClickListener {
            activity?.currentFocus?.clearFocus()
            it.hideKeyBoard()
        }
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
                        homeViewModel.userId = Firebase.auth.currentUser?.uid ?: ""
                        homeViewModel.getWatchedMovies(homeViewModel.userId)
                        fetchData()
                    } else {
                        hideProgressBar()
                        Toast.makeText(requireContext(), "Email or Password is invalid!!", Toast.LENGTH_SHORT).show()
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
                    nameLayout.visibility = View.VISIBLE

                }
                isSignIn = false
            } else {
                binding.apply {
                    textView.text = "Not Registered Yet, Sign Up !"
                    textView.visibility = View.VISIBLE
                    button.text = "SIGN IN"
                    confirmPasswordLayout.visibility = View.GONE
                    nameLayout.visibility = View.GONE
                }
                isSignIn = true
            }
            activity?.currentFocus?.clearFocus()
        }
    }

    private fun fetchData() {
        homeViewModel.moviesWithGenre.observe(viewLifecycleOwner, Observer {respone ->
            when (respone) {
                is NetWorkResult.Success -> {
                    countLoad++
                    navigateToHome()
                }
                is NetWorkResult.Loading -> {}
                is NetWorkResult.Error -> {
                    AppUtils.showDialogError(requireContext())
                }
            }
        })

        homeViewModel.genresResponse.observe(viewLifecycleOwner, Observer {respone ->
            when (respone) {
                is NetWorkResult.Success -> {
                    countLoad++
                    navigateToHome()
                }
                is NetWorkResult.Loading -> {}
                is NetWorkResult.Error -> {
                    AppUtils.showDialogError(requireContext())
                }
            }
        })

        homeViewModel.popularTodayResponse.observe(viewLifecycleOwner, Observer {respone ->
            when (respone) {
                is NetWorkResult.Success -> {
                    countLoad++
                    navigateToHome()
                }
                is NetWorkResult.Loading -> {}
                is NetWorkResult.Error -> {
                    AppUtils.showDialogError(requireContext())
                }
            }
        })
    }

    private fun navigateToHome() {
        if (countLoad == 3) {
            val navOption = NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_loginFragment_to_homeFragment,
                null,
                navOption
            )
        }
    }

}

private fun View.hideKeyBoard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}
