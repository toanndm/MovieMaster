package com.example.movieinfomation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.movieinfomation.R
import com.example.movieinfomation.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        setContentView(view)
        binding.bottomNavigation.setupWithNavController(navHostFragment.navController)
        binding.bottomNavigation.setOnItemReselectedListener { /* NO-OP */ }
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.searchFragment, R.id.accountFragment
                    -> binding.bottomNavigation.visibility = View.VISIBLE
                else -> binding.bottomNavigation.visibility = View.GONE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Firebase.auth.signOut()
    }
}