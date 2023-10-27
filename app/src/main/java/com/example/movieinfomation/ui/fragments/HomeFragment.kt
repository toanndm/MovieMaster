package com.example.movieinfomation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.movieinfomation.R
import com.example.movieinfomation.adapters.CustomSpinnerAdapter
import com.example.movieinfomation.databinding.FragmentHomeBinding
import com.example.movieinfomation.other.Constants

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCustomSpinner()
    }

    private fun setCustomSpinner() {
        val customSpinnerAdapter = CustomSpinnerAdapter(
            requireContext(),
            R.layout.item_spinner,
            getList()
        )
        binding.spinnerCategories.adapter = customSpinnerAdapter

    }

    private fun getList(): List<CustomSpinnerAdapter.Category> {
        val categories = mutableListOf<CustomSpinnerAdapter.Category>()

        val names = resources.getStringArray(R.array.categories).apply {
            toList()
        }

        for (name in names) {
            categories.add(CustomSpinnerAdapter.Category(name))
        }

        return categories
    }

}