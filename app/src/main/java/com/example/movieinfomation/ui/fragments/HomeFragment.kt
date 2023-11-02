package com.example.movieinfomation.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.movieinfomation.R
import com.example.movieinfomation.adapters.CustomSpinnerAdapter
import com.example.movieinfomation.adapters.MovieRecyclerAdapter
import com.example.movieinfomation.adapters.ViewPagerAdapter
import com.example.movieinfomation.databinding.FragmentHomeBinding
import com.example.movieinfomation.models.Genre
import com.example.movieinfomation.models.Movie
import com.example.movieinfomation.other.NetWorkResult
import com.example.movieinfomation.ui.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var timer: Timer
    private lateinit var binding: FragmentHomeBinding
    private lateinit var listGenres: List<Genre>

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
        subscribeToObserve()
        selectSpinner()
    }


    private fun subscribeToObserve() {

        homeViewModel.moviesWithGenre.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is NetWorkResult.Success -> {
                    response.data?.let {
                        val list = it.movieItems
                        setRecycleView(list)
                    }

                }
                is NetWorkResult.Loading -> {

                }
                is NetWorkResult.Error -> {
                    showDialogError()
                }
            }
        })

        homeViewModel.genresResponse.observe(viewLifecycleOwner, Observer {response ->
            when (response) {
                is NetWorkResult.Success -> {
                    response.data?.let {
                        listGenres = it.genres
                        val categories = mutableListOf<CustomSpinnerAdapter.Category>()
                        for (genre in it.genres) {
                            categories.add(CustomSpinnerAdapter.Category(genre.name))
                        }
                        setCustomSpinner(categories)
                    }
                }
                is NetWorkResult.Loading -> {

                }
                is NetWorkResult.Error -> {
                    showDialogError()
                }
            }
        })

        homeViewModel.popularTodayResponse.observe(viewLifecycleOwner, Observer {response ->
            when (response) {
                is NetWorkResult.Success -> {
                    response.data?.let {
                        val adapter = ViewPagerAdapter(it.movieItems.take(5), requireContext())
                        binding.viewpagerPopularMovie.adapter = adapter
                        val compositePageTransformer = CompositePageTransformer().apply {
                            addTransformer(MarginPageTransformer(40))
                            addTransformer(ViewPager2.PageTransformer { page, position ->
                                val r: Float = 1f - Math.abs(position)
                                page.scaleY = 0.85f + r * 0.15f
                            })
                        }
                        binding.circleIndicator.setViewPager(binding.viewpagerPopularMovie)
                        binding.viewpagerPopularMovie.apply {
                            offscreenPageLimit = 3
                            clipToPadding = false
                            clipChildren = false
                            setPageTransformer(compositePageTransformer)
                        }
                        autoSlideImage()
                    }
                }
                is NetWorkResult.Loading -> {

                }
                is NetWorkResult.Error -> {
                    showDialogError()
                }
            }

        })
    }

    private fun setRecycleView(list: List<Movie>) {
        binding.rcvCategorizedMovie.apply {
            adapter = MovieRecyclerAdapter(list, requireContext())
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

    }

    private fun autoSlideImage() {
        timer = Timer()
        timer.schedule(object: TimerTask(){
            var isPlus = true
            var isFirstRun = true
            override fun run() {
                Handler(Looper.getMainLooper()).post(Runnable {
                    if (isFirstRun) {
                        isFirstRun = false
                    } else {
                        var currentItem: Int = binding.viewpagerPopularMovie.currentItem
                        val totalItem: Int = 4
                        if (isPlus) {
                            currentItem++
                            binding.viewpagerPopularMovie.currentItem = currentItem
                            if (currentItem >= totalItem) {
                                isPlus = false
                            }
                        } else {
                            currentItem--
                            binding.viewpagerPopularMovie.currentItem = currentItem
                            if (currentItem <= 0) {
                                isPlus = true
                            }
                        }
                    }
                })
            }
        }, 500, 2500)
    }

    private fun showDialogError() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
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

    private fun setCustomSpinner(list: List<CustomSpinnerAdapter.Category>) {
        val customSpinnerAdapter = CustomSpinnerAdapter(
            requireContext(),
            R.layout.item_spinner,
            list
        )
        binding.spinnerCategories.adapter = customSpinnerAdapter

    }

    private fun selectSpinner(){
        binding.spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                homeViewModel.getMoviesWithGenre(listGenres[position].id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

}