package com.example.movieinfomation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.movieinfomation.R
import com.example.movieinfomation.other.AppUtils
import com.example.movieinfomation.other.NetWorkResult
import com.example.movieinfomation.ui.viewmodels.HomeViewModel

class SplashFragment : Fragment(R.layout.fragment_splash) {
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var countLoad: Int = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
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
                .setPopUpTo(R.id.splashFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_splashFragment_to_homeFragment,
                null,
                navOption
            )
        }
    }

}