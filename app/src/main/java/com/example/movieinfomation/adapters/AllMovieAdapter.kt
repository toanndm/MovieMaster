package com.example.movieinfomation.adapters

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movieinfomation.R
import com.example.movieinfomation.models.Movie
import com.google.android.material.animation.AnimatableView.Listener
import jp.wasabeef.glide.transformations.BlurTransformation

class AllMovieAdapter(
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

    private val differCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    private var onItemClickListener: ((Movie) -> Unit)? = null

    fun setOnItemClickListener(listener: (Movie) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMovieViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_movie_item, parent, false)
        return AllMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllMovieViewHolder, position: Int) {
        val movie = differ.currentList[position]
        val url = movie.posterPath
        Glide
            .with(context)
            .load("https://image.tmdb.org/t/p/original/$url")
            .error(R.drawable.not_available)
            .centerCrop()
            .into(holder.imgPoster)
        holder.tvRate.text = movie.voteAverage.toString()
        holder.tvTitle.text = movie.title
        holder.tvDate.text = movie.releaseDate
        val space = ", "
        val idGenres = movie.genre
        var textGenres = ""
        if (idGenres.isNotEmpty()) {
            textGenres += idToName[idGenres[0]]
            for (i in 1..<idGenres.size) {
                textGenres = textGenres + space + idToName[idGenres[i]]
            }
        }
        holder.tvGenres.text = textGenres
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(movie)
            }

        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}