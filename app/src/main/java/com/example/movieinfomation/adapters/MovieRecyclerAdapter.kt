package com.example.movieinfomation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieinfomation.R
import com.example.movieinfomation.models.Movie
import com.google.android.material.animation.AnimatableView.Listener

class MovieRecyclerAdapter(
    private val list: List<Movie>,
    private val context: Context
): RecyclerView.Adapter<MovieRecyclerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgView: ImageView = itemView.findViewById(R.id.imgPoster1)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle1)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate1)
        val tvRate: TextView = itemView.findViewById(R.id.tvRate1)
    }

    private var onItemClickListener: ((Movie) -> Unit)? = null

    fun setOnItemClickListener(listener: (Movie) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val url = list[position].posterPath
        Glide
            .with(context)
            .load("https://image.tmdb.org/t/p/original/$url")
            .centerCrop()
            .into(holder.imgView)
        holder.tvRate.text = list[position].voteAverage.toString()
        holder.tvTitle.text = list[position].title
        holder.tvDate.text = list[position].releaseDate

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(list[position])
            }
        }
    }
}