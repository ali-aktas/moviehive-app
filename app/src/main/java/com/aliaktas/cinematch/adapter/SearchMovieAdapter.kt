package com.aliaktas.cinematch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aliaktas.cinematch.R
import com.aliaktas.cinematch.data.model.Movie
import com.aliaktas.cinematch.databinding.ItemSearchMovieBinding
import com.bumptech.glide.Glide

class SearchMovieAdapter : ListAdapter<Movie, SearchMovieAdapter.SearchMovieViewHolder>(
    object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
) {
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

    inner class SearchMovieViewHolder(private val binding: ItemSearchMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && isClickValid()) {
                    onMovieClick?.invoke(getItem(position))
                }
            }
        }

        fun bind(movie: Movie) {
            binding.apply {
                ivMovieTitle.text = movie.title

                // Poster URL oluşturma
                val posterUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"

                Glide.with(ivMoviePoster)
                    .load(posterUrl)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(ivMoviePoster)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMovieViewHolder {
        val binding = ItemSearchMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchMovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}