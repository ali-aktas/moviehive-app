package com.aliaktas.cinematch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aliaktas.cinematch.R
import com.aliaktas.cinematch.data.model.Movie
import com.aliaktas.cinematch.databinding.ItemMovieBinding
import com.bumptech.glide.Glide

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

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

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onMovieClick?.invoke(movies[position])
                }
            }
        }

        fun bind(movie: Movie) {
            binding.apply {
                tvTitle.text = movie.title
                val posterUrl = "https://image.tmdb.org/t/p/w500${movie.posterPath}"
                Glide.with(ivPoster)
                    .load(posterUrl)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(ivPoster)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size
}