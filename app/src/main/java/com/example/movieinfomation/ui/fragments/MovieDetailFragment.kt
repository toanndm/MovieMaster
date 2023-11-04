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
                val url = it.homePage
                val uri = Uri.parse(url)
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }
    }

    private fun clickPlay() {
        binding.btnPlay.setOnClickListener {
            movieDetail?.let {
                if (it.hasVideo) {
                    binding.tvNotification.text =
                        "Sorry, this feature Sorry, this feature is currently under development and may not be fully functional."

                } else {
                    binding.tvNotification.text = "Sorry, this movie doesn't have a video."
                }
                binding.tvNotification.visibility = View.VISIBLE
            }
        }

    }

    private fun clickBack() {
        binding.btnBack1.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun subscribeToObserve() {
        homeViewModel.movieDetail.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is NetWorkResult.Success -> {
                    response.data?.let {
                        movieDetail = it
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