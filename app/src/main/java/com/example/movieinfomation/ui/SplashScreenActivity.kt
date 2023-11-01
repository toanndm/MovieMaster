package com.example.movieinfomation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.movieinfomation.R
import com.example.movieinfomation.adapters.CustomSpinnerAdapter
import com.example.movieinfomation.other.NetWorkResult
import com.example.movieinfomation.ui.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private val homeViewModel: HomeViewModel by viewModels()
    private var countLoad: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        fetchData()



    }

    private fun fetchData() {
        homeViewModel.moviesWithGenre.observe(this, Observer {respone ->
            when (respone) {
                is NetWorkResult.Success -> {
                    countLoad++
                    navigateToMain()
                }
                is NetWorkResult.Loading -> {

                }
                is NetWorkResult.Error -> {
                    showDialogError()
                }
            }
        })

        homeViewModel.genresResponse.observe(this, Observer {respone ->
            when (respone) {
                is NetWorkResult.Success -> {
                    countLoad++
                    navigateToMain()
                }
                is NetWorkResult.Loading -> {

                }
                is NetWorkResult.Error -> {
                    showDialogError()
                }
            }
        })

        homeViewModel.popularTodayResponse.observe(this, Observer {respone ->
            when (respone) {
                is NetWorkResult.Success -> {
                    countLoad++
                    navigateToMain()
                }
                is NetWorkResult.Loading -> {

                }
                is NetWorkResult.Error -> {
                    showDialogError()
                }
            }
        })
    }

    private fun navigateToMain() {
        if (countLoad == 3) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showDialogError() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage("Sorry! Unable to download data. Please check your network connection or try again later.")
            .setTitle("Error!!!")
            .setPositiveButton("Wait") { dialog, _ ->
                dialog.cancel()
            }
            .setNegativeButton("Close app") { _, _ ->
                android.os.Process.killProcess(android.os.Process.myPid())

            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}