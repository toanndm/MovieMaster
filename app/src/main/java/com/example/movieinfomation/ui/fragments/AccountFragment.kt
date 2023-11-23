package com.example.movieinfomation.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.movieinfomation.R
import com.example.movieinfomation.databinding.FragmentAccountBinding
import com.example.movieinfomation.ui.viewmodels.HomeViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class AccountFragment : Fragment(R.layout.fragment_account) {
    private lateinit var binding: FragmentAccountBinding
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val user = Firebase.auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserInfo()
        clickApplyProfile()
        clickChooseImage()
        subscribeToObserve()
        changePassword()
    }

    private fun changePassword() {
        binding.btnChange.setOnClickListener {
            val pass = binding.passwordEtProfile.text.toString()
            val newPass = binding.newPasswordEtProfile.text.toString()
            if (pass.isNotEmpty() && newPass.isNotEmpty() && pass == newPass && pass.length > 6) {
                user!!.updatePassword(newPass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Timber.d("Changed password")
                            binding.passwordEtProfile.setText("")
                            binding.newPasswordEtProfile.setText("")
                            activity?.currentFocus?.clearFocus()
                            Toast.makeText(requireContext(), "Change password success!", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Password invalid!!", Toast.LENGTH_SHORT).show()
                activity?.currentFocus?.clearFocus()
            }
        }
    }

    private fun subscribeToObserve() {
        homeViewModel.imageUri.observe(viewLifecycleOwner) {uri ->
            uri?.let {
                binding.imgProfile.setImageURI(it)
                homeViewModel.uploadImage(it)
            }
        }
        homeViewModel.imageLink.observe(viewLifecycleOwner) {url ->
            val profileUpdates = userProfileChangeRequest {
                photoUri = Uri.parse(url)
            }

            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Timber.tag("updateURL").d("Update success")
                    }
                }
        }
    }

    private fun clickChooseImage() {
        binding.tvUpload.setOnClickListener {
            chooseImage()
        }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImage.launch(intent)
    }
    private var pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        try {
            homeViewModel.imageUri.value = result.data?.data

        } catch (e: Exception) {
            Timber.d("Image picker: $e")
        }
    }


    private fun clickApplyProfile() {
        val phone = binding.phoneEtProfile.text.toString()
        val name = binding.nameEtProfile.text.toString()
        if (phone.isNotEmpty() && name.isNotEmpty()) {
            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }

            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Timber.tag("updateURL").d("Update success")
                    }
                }
        }

    }

    private fun setUserInfo() {
        user?.let {
            val email = it.email
            val name = it.displayName
            val url = it.photoUrl
            it.phoneNumber?.let { phoneNumber ->
                if (phoneNumber.isNotEmpty()) {
                    binding.phoneEtProfile.setText(phoneNumber)
                }
            }
            binding.emailEtProfile.setText(email)
            binding.nameEtProfile.setText(name)
            Glide
                .with(requireContext())
                .load(url)
                .error(R.drawable.ic_account)
                .centerCrop()
                .into(binding.imgProfile)
        }
    }

}