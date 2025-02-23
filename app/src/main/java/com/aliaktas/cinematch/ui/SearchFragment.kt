package com.aliaktas.cinematch.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.aliaktas.cinematch.R
import com.aliaktas.cinematch.adapter.SearchMovieAdapter
import com.aliaktas.cinematch.data.model.Movie
import com.aliaktas.cinematch.databinding.FragmentSearchBinding
import com.aliaktas.cinematch.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var isClickable = true
    private val viewModel: SearchViewModel by viewModels()
    private val searchMovieAdapter = SearchMovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchInput()
        observeViewModel()
    }

    private fun handleMovieClick(movie: Movie) {
        if (isClickable) {
            isClickable = false
            val action = SearchFragmentDirections.actionSearchToDetails(movie.id)
            findNavController().navigate(action)
            // 300ms sonra tekrar tıklamaya izin ver
            view?.postDelayed({ isClickable = true }, 300)
        }
    }

    private fun setupRecyclerView() {
        binding.rvSearchResults.apply {
            adapter = searchMovieAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }
        // Movie details
        searchMovieAdapter.onMovieClick = { movie ->
            handleMovieClick(movie)
        }
    }


    private fun setupSearchInput() {
        binding.etSearch.addTextChangedListener { text ->
            // Avoid searching blank string
            text?.let {
                if (it.toString().length > 1) {
                    viewModel.searchMovies(it.toString())
                }
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.searchResults.collect { movies ->
                        // Update the results
                        searchMovieAdapter.submitList(movies)

                        // Update UI
                        //binding.tvNoResults.isVisible = movies.isEmpty()
                        binding.rvSearchResults.isVisible = movies.isNotEmpty()
                    }
                }

                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.progressBar.isVisible = isLoading
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}