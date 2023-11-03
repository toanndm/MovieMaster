package com.example.movieinfomation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieinfomation.R
import com.example.movieinfomation.adapters.AllMovieAdapter
import com.example.movieinfomation.adapters.CustomSpinnerAdapter
import com.example.movieinfomation.adapters.MovieRecyclerAdapter
import com.example.movieinfomation.databinding.FragmentAllMovieBinding
import com.example.movieinfomation.models.Movie
import com.example.movieinfomation.other.AppUtils
import com.example.movieinfomation.other.NetWorkResult
import com.example.movieinfomation.ui.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AllMovieFragment : Fragment(R.layout.fragment_all_movie) {
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var binding: FragmentAllMovieBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObserve()
    }

    private fun subscribeToObserve() {
        homeViewModel.query.observe(viewLifecycleOwner, Observer {
            binding.edtSearch1.setText(it)
        })

        homeViewModel.isSearching.observe(viewLifecycleOwner, Observer { isSearching ->
            if (isSearching) {
                binding.tvSearchResult.visibility = View.VISIBLE
            } else {
                binding.tvSearchResult.visibility = View.GONE
            }
        })

        homeViewModel.moviesSearch.observe(viewLifecycleOwner, Observer {response ->
            when (response) {
                is NetWorkResult.Success -> {
                    response.data?.let {
                        binding.tvSearchResult.text = "Total results: ${it.totalResults}"
                        val list = it.movieItems
                        setRecycleView(list, homeViewModel.idToGenre!!)
                    }

                }
                is NetWorkResult.Loading -> {

                }
                is NetWorkResult.Error -> {
                    AppUtils.showDialogError(requireContext())
                }
            }
        })
    }


    private fun setRecycleView(list: List<Movie>, idToGenre: Map<Int, String>) {
        binding.rcvAllMovie.apply {
            adapter = AllMovieAdapter(list, idToGenre, requireContext())
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

    }

    override fun onPause() {
        super.onPause()
        homeViewModel.query.value = ""
    }
}