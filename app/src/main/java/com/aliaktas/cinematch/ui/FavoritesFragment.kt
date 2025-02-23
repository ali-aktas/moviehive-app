package com.aliaktas.cinematch.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aliaktas.cinematch.R
import com.aliaktas.cinematch.adapter.FavoriteMovieAdapter
import com.aliaktas.cinematch.data.model.Movie
import com.aliaktas.cinematch.databinding.FragmentFavoritesBinding
import com.aliaktas.cinematch.viewmodel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private var isClickable = true
    private val viewModel: FavoritesViewModel by viewModels()
    private val favoriteMovieAdapter = FavoriteMovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    private fun handleMoviewClick(movie: Movie) {
        if (isClickable) {
            isClickable = false
            val action = FavoritesFragmentDirections.actionFavoritesToDetails(movie.id)
            findNavController().navigate(action)
            view?.postDelayed({ isClickable = true }, 300)
        }
    }

    private fun setupRecyclerView() {
        binding.rvFavoriteMovies.apply {
            adapter = favoriteMovieAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        favoriteMovieAdapter.onMovieClick = { movie ->
            handleMoviewClick(movie)
        }

        favoriteMovieAdapter.onRemoveFavorite = { movie ->
            viewModel.removeFromFavorites(movie)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favorites.collect { favorites ->
                    favoriteMovieAdapter.submitList(null)
                    favoriteMovieAdapter.submitList(favorites)
                    binding.tvNoFavorites.isVisible = favorites.isEmpty()
                    binding.rvFavoriteMovies.isVisible = favorites.isNotEmpty()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
