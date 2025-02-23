package com.aliaktas.cinematch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aliaktas.cinematch.data.model.Genre
import com.aliaktas.cinematch.databinding.ItemGenreBinding

class GenreAdapter : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    var genres: List<Genre> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var selectedGenreId: Int? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onGenreClick: ((Genre) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = ItemGenreBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bind(genres[position])
    }

    override fun getItemCount() = genres.size

    inner class GenreViewHolder(
        private val binding: ItemGenreBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: Genre) {
            binding.tvGenreName.text = genre.name
            binding.root.isSelected = genre.id == selectedGenreId

            binding.root.setOnClickListener {
                onGenreClick?.invoke(genre)
            }
        }
    }
}