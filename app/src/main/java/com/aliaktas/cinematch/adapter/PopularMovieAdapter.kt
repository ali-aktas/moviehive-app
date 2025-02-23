package com.aliaktas.cinematch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aliaktas.cinematch.data.model.Movie
import com.aliaktas.cinematch.databinding.ItemPopularMovieBinding
import com.bumptech.glide.Glide

class PopularMovieAdapter : RecyclerView.Adapter<PopularMovieAdapter.PopularMovieViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var movies: List<Movie>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    var onMovieClick: ((Movie) -> Unit)? = null

    // Sadece bu kısmı ekledik - tıklama kontrolü için
    private var lastClickTime = 0L
    private fun isClickValid(): Boolean {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > 300) {
            lastClickTime = currentTime
            return true
        }
        return false
    }

    inner class PopularMovieViewHolder(private val binding: ItemPopularMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                // Burada isClickValid() kontrolünü ekledik
                if (position != RecyclerView.NO_POSITION && isClickValid()) {
                    onMovieClick?.invoke(movies[position])
                }
            }
        }

        fun bind(movie: Movie) {
            binding.apply {
                val posterUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"
                Glide.with(ivPoster)
                    .load(posterUrl)
                    .centerCrop()
                    .into(ivPoster)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMovieViewHolder {
        val binding = ItemPopularMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PopularMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularMovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size
}