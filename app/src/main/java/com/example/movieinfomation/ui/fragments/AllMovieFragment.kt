package com.example.movieinfomation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AbsListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieinfomation.R
import com.example.movieinfomation.adapters.AllMovieAdapter
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
    private lateinit var movieAdapter: AllMovieAdapter
    private var listMovie: MutableList<Movie> = mutableListOf()

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
        setRecycleView(homeViewModel.idToGenre!!)
        clickSearch()
        setOnItemClick()
        setOnClickBack()
        subscribeToObserve()
    }

    private fun setOnClickBack() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setOnItemClick() {
        movieAdapter.setOnItemClickListener {
            homeViewModel.getMovieDetail(it.id)
            homeViewModel.addWatchedMovie(homeViewModel.userId, it)
            findNavController().navigate(R.id.action_allMovieFragment_to_movieDetailFragment)
        }
    }

    private fun clickSearch() {
        binding.edtSearch1.setOnEditorActionListener(TextView.OnEditorActionListener { view, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                if (view.text.isNotEmpty()) {
                    homeViewModel.query.value = view.text.toString()
                    homeViewModel.isSearching.value = true
                }
            }
            false
        })
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = binding.rcvAllMovie.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount


            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount

            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 20
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                if (homeViewModel.isSearching.value == true) {
                    homeViewModel.getMoviesSearch(homeViewModel.query.value!!)
                } else {
                    homeViewModel.getAllMovies()
                }

                isScrolling = false
            }
        }
    }

    private fun subscribeToObserve() {
        homeViewModel.query.observe(viewLifecycleOwner, Observer {
            homeViewModel.searchPage = 0
            listMovie = mutableListOf()
            homeViewModel.getMoviesSearch(it)
            binding.edtSearch1.setText(it)
        })

        homeViewModel.isSearching.observe(viewLifecycleOwner, Observer { isSearching ->
            listMovie = mutableListOf()
            if (isSearching) {
                binding.tvSearchResult.visibility = View.VISIBLE
            } else {
                binding.tvSearchResult.visibility = View.GONE
            }
        })

        homeViewModel.allMovies.observe(viewLifecycleOwner, Observer {response ->
            when (response) {
                is NetWorkResult.Success -> {
                    response.data?.let {
                        hideProgressBar()
                        val list = it.movieItems
                        listMovie.addAll(list)
                        movieAdapter.differ.submitList(listMovie.toList())
                    }
                }
                is NetWorkResult.Loading -> {
                    showProgressBar()
                }
                is NetWorkResult.Error -> {
                    AppUtils.showDialogError(requireContext())
                }
            }
        })

        homeViewModel.moviesSearch.observe(viewLifecycleOwner, Observer {response ->
            when (response) {
                is NetWorkResult.Success -> {
                    response.data?.let {
                        binding.tvSearchResult.text = "Total results: ${it.totalResults}"
                        hideProgressBar()
                        val list = it.movieItems
                        listMovie.addAll(list)
                        movieAdapter.differ.submitList(listMovie.toList())
                    }
                }
                is NetWorkResult.Loading -> {
                    showProgressBar()
                }
                is NetWorkResult.Error -> {
                    AppUtils.showDialogError(requireContext())
                }
            }
        })
    }

    private fun showProgressBar() {
        binding.loadingContainer.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideProgressBar() {
        binding.loadingContainer.visibility = View.GONE
        isLoading = false
    }


    private fun setRecycleView(idToGenre: Map<Int, String>) {
        movieAdapter = AllMovieAdapter(idToGenre, requireContext())
        binding.rcvAllMovie.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addOnScrollListener(scrollListener)
        }

    }

    override fun onPause() {
        super.onPause()
        homeViewModel.query.value = ""
        homeViewModel.searchPage = 0
        homeViewModel.allMoviesPage = 0
        listMovie = mutableListOf()
    }

    override fun onStop() {
        super.onStop()
        homeViewModel.query.value = ""
        homeViewModel.searchPage = 0
        homeViewModel.allMoviesPage = 0
        listMovie = mutableListOf()
    }
}