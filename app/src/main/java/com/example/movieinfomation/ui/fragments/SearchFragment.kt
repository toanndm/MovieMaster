package com.example.movieinfomation.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieinfomation.R
import com.example.movieinfomation.adapters.MovieRecyclerAdapter
import com.example.movieinfomation.databinding.FragmentSearchBinding
import com.example.movieinfomation.models.Movie
import com.example.movieinfomation.other.NetWorkResult
import com.example.movieinfomation.ui.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toggleButtonGroup.selectButton(R.id.btnToday)
        subscribeToObserve()
        setOnClickButton()
        completeSearch()
        clickSeeAll()
    }

    private fun setOnClickButton() {
        binding.btnToday.setOnClickListener(View.OnClickListener {
            homeViewModel.getMoviesTrending("day")
        })
        binding.btnThisWeek.setOnClickListener(View.OnClickListener {
            homeViewModel.getMoviesTrending("week")
        })
    }

    private fun subscribeToObserve() {
        homeViewModel.moviesTrendingDay.observe(viewLifecycleOwner, Observer { response ->
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

        homeViewModel.moviesNowPlaying.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is NetWorkResult.Success -> {
                    response.data?.let {
                        val list = it.movieItems
                        setRecycleViewRcm(list)
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

    private fun setRecycleViewRcm(list: List<Movie>) {
        binding.rcvRecommend.apply {
            adapter = MovieRecyclerAdapter(list, requireContext())
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
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

    private fun setRecycleView(list: List<Movie>) {
        binding.rcvTrending.apply {
            adapter = MovieRecyclerAdapter(list, requireContext())
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

    }

    private fun completeSearch() {
        binding.edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { view, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                if (view.text != "") {
                    homeViewModel.query.value = view.text.toString()
                    homeViewModel.getMoviesSearch(view.text.toString())
                    homeViewModel.isSearching.value = true
                    findNavController().navigate(R.id.action_searchFragment_to_allMovieFragment)
                }
            }
            false
        })
    }

    private fun clickSeeAll() {
        binding.tvSeeAll.setOnClickListener { View.OnClickListener {

        } }
    }

}