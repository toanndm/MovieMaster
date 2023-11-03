package com.example.movieinfomation.adapters

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieinfomation.R
import com.example.movieinfomation.models.Movie

class AllMovieAdapter(
    private val list: List<Movie>,
    private val idToName: Map<Int, String>,
    private val context: Context
): RecyclerView.Adapter<AllMovieAdapter.AllMovieViewHolder>() {
    class AllMovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imgPoster: ImageView = itemView.findViewById(R.id.imgPoster2)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle2)
        val tvGenres: TextView = itemView.findViewById(R.id.tvGenres2)
        val tvRate: TextView = itemView.findViewById(R.id.tvRate2)
        val tvDate: TextView = itemView.findViewById(R.id.tvReleaseDate2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_movie_item, parent, false)
        return AllMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllMovieViewHolder, position: Int) {
        val url = list[position].posterPath
        Glide
            .with(context)
            .load("https://image.tmdb.org/t/p/original/$url")
            .centerCrop()
            .into(holder.imgPoster)
        holder.tvRate.text = list[position].voteAverage.toString()
        holder.tvTitle.text = list[position].title
        holder.tvDate.text = list[position].releaseDate
        val space = ", "
        val idGenres = list[position].genre
        var textGenres = ""
        if (idGenres.isNotEmpty()) {
            textGenres += idToName[idGenres[0]]
            for (i in 1..<idGenres.size) {
                textGenres = textGenres + space + idToName[idGenres[i]]
            }
        }
        holder.tvGenres.text = textGenres
    }

    override fun getItemCount(): Int {
        return list.size
    }
}