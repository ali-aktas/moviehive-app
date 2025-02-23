package com.aliaktas.cinematch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aliaktas.cinematch.R
import com.aliaktas.cinematch.data.model.Movie
import com.aliaktas.cinematch.databinding.ItemFavoriteMovieBinding
import com.bumptech.glide.Glide

class FavoriteMovieAdapter : ListAdapter<Movie, FavoriteMovieAdapter.FavoriteMovieViewHolder>(
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
    var onRemoveFavorite: ((Movie) -> Unit)? = null

    inner class FavoriteMovieViewHolder(private val binding: ItemFavoriteMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onMovieClick?.invoke(getItem(position))
                }
            }

            binding.btnRemoveFavorite.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onRemoveFavorite?.invoke(getItem(position))
                }
            }
        }

        fun bind(movie: Movie) {
            binding.apply {
                tvMovieTitle.text = movie.title
                tvReleaseDate.text = movie.releaseDate
                tvDetails.text = movie.overview

                val posterUrl = "https://image.tmdb.org/t/p/w780${movie.posterPath}"

                Glide.with(ivMoviePoster)
                    .load(posterUrl)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(ivMoviePoster)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMovieViewHolder {
        val binding = ItemFavoriteMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}