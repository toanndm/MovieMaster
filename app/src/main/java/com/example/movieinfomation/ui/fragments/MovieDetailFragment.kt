package com.example.movieinfomation.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.movieinfomation.R
import com.example.movieinfomation.databinding.FragmentMovieDetailBinding
import com.example.movieinfomation.models.MovieDetail
import com.example.movieinfomation.other.AppUtils
import com.example.movieinfomation.other.NetWorkResult
import com.example.movieinfomation.ui.viewmodels.HomeViewModel
import timber.log.Timber

class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {
    private lateinit var binding: FragmentMovieDetailBinding
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var movieDetail: MovieDetail? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObserve()
        clickBack()
        clickPlay()
        clickWebsite()
    }

    private fun clickWebsite() {
        binding.btnOpenLink.setOnClickListener {
            movieDetail?.let {
                val url = "https://www.themoviedb.org/movie/" + it.id
                val uri = Uri.parse(url)
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }
    }

    private fun clickPlay() {
        binding.btnPlay.setOnClickListener {
            movieDetail?.let {
                homeViewModel.getMoviesSimilar(it.id)
                homeViewModel.getMovieVideos(it.id)
                findNavController().navigate(R.id.action_movieDetailFragment_to_playTrailerFragment)
            }
        }

    }

    private fun clickBack() {
        binding.btnBack1.setOnClickListener {
            val size = homeViewModel.listMovieBackStack.size
            if (size > 1) {
                homeViewModel.getMovieDetail(homeViewModel.listMovieBackStack[size - 2])
                homeViewModel.getMovieVideos(homeViewModel.listMovieBackStack[size - 2])
                Timber.tag("test stack").d("-2 " + homeViewModel.listMovieBackStack[size - 2].toString())
            }
            homeViewModel.popBackStack()
            findNavController().popBackStack()
            Timber.tag("test stack").d("move to play and"+homeViewModel.listMovieBackStack.toString())
        }
    }

    private fun subscribeToObserve() {
        homeViewModel.movieDetail.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is NetWorkResult.Success -> {
                    response.data?.let {
                        movieDetail = it
                        if (homeViewModel.listMovieBackStack.isEmpty()) {
                            homeViewModel.listMovieBackStack.add(it.id)
                        }
                        hideProgressBar()
                        bindData(it)
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

    private fun hideProgressBar() {
        binding.progressBarDetail.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBarDetail.visibility = View.VISIBLE
    }

    private fun bindData(movie: MovieDetail) {
        binding.apply {
            tvTitle3.text = movie.title
            tvContent.text = movie.overView
            val genre = movie.genres?.let {
                it[0].name
            } ?: "Loading"
            tvGenre.text = genre
            tvRate3.text = movie.voteAverage.toString()
            tvRunTime.text = movie.runTime.toString() + " Minutes"
            tvYear.text = movie.releaseDate.take(4)
        }
        val urlBackDrop = movie.backdropPath
        val urlPoster = movie.posterPath
        Glide
            .with(requireContext())
            .load("https://image.tmdb.org/t/p/original/$urlBackDrop")
            .error(R.drawable.not_available)
            .centerCrop()
            .into(binding.imgBackdrop)

        Glide
            .with(requireContext())
            .load("https://image.tmdb.org/t/p/original/$urlPoster")
            .error(R.drawable.not_available)
            .centerCrop()
            .into(binding.imgPoster3)
    }


}