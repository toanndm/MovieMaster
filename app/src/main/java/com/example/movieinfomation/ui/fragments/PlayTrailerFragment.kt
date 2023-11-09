package com.example.movieinfomation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieinfomation.R
import com.example.movieinfomation.adapters.AllMovieAdapter
import com.example.movieinfomation.databinding.FragmentPlayTrailerBinding
import com.example.movieinfomation.models.Movie
import com.example.movieinfomation.other.AppUtils
import com.example.movieinfomation.other.NetWorkResult
import com.example.movieinfomation.ui.viewmodels.HomeViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import timber.log.Timber

class PlayTrailerFragment: Fragment(R.layout.fragment_play_trailer) {
    private lateinit var binding: FragmentPlayTrailerBinding
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayTrailerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(binding.youtubePlayer)
        clickBack()
        subscribeToObserve()
    }

    private fun subscribeToObserve() {
        homeViewModel.moviesSimilar.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is NetWorkResult.Success -> {
                    response.data?.let {
                        hideProgressBar()
                        val list = it.movieItems
                        setRecyclerView(list)
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

        homeViewModel.movieVideos.observe(viewLifecycleOwner, Observer {response ->
            when (response) {
                is NetWorkResult.Success -> {
                    response.data?.let {
                        hideProgressBar()
                        binding.youtubePlayer.addYouTubePlayerListener(object :
                            AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                val video = it.videos[0].keyVideo
                                youTubePlayer.loadVideo(video, 0F)
                            }
                        })
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
        homeViewModel.movieTitle.observe(viewLifecycleOwner, Observer {
            binding.tvTitlePlayer.text = it?:"Something went wrong!!"
        })

    }

    private fun setRecyclerView(list: MutableList<Movie>) {
        val moviesAdapter = AllMovieAdapter(homeViewModel.idToGenre!!, requireContext()).apply {
            differ.submitList(list)
            setOnItemClickListener {
                homeViewModel.getMovieDetail(it.id)
                homeViewModel.listMovieBackStack.add(it.id)
                findNavController().navigate(R.id.action_playTrailerFragment_to_movieDetailFragment)
            }
        }
        binding.rcvAllMovieRcm.apply {
            adapter = moviesAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private fun clickBack() {
        binding.btnBackYoutube.setOnClickListener {
            homeViewModel.getMovieDetail(homeViewModel.listMovieBackStack[homeViewModel.listMovieBackStack.size - 1])
            findNavController().popBackStack()
        }
    }

    private fun showProgressBar() {
        binding.loadingVideo.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.loadingVideo.visibility = View.GONE
    }

}