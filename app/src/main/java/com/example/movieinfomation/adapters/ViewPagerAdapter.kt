package com.example.movieinfomation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LAYER_TYPE_HARDWARE
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.movieinfomation.R
import com.example.movieinfomation.models.Movie
import com.example.movieinfomation.models.MovieResponse

class ViewPagerAdapter(private val movies: List<Movie>,
                       private val context: Context): RecyclerView.Adapter<ViewPagerAdapter.PopularMovieViewHolder>() {

    class PopularMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgView: ImageView = itemView.findViewById(R.id.imgViewpager)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitlePopularMovieToday)
        val tvDate: TextView = itemView.findViewById(R.id.tvReleaseDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_viewpager, parent, false)
        return PopularMovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: PopularMovieViewHolder, position: Int) {
        val url = movies[position].posterPath
        Glide
            .with(context)
            .load("https://image.tmdb.org/t/p/original/$url")
            .centerCrop()
            .into(holder.imgView)
        holder.tvTitle.text = movies[position].title
        holder.tvDate.text = movies[position].releaseDate
    }
}