package com.aliaktas.cinematch.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aliaktas.cinematch.R
import com.aliaktas.cinematch.adapter.PopularMovieAdapter
import com.aliaktas.cinematch.databinding.FragmentMovieListBinding
import com.aliaktas.cinematch.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels() // Aynı ViewModel'i kullanıyoruz

    private lateinit var movieAdapter: PopularMovieAdapter // Popüler filmler için adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter'ı başlat
        movieAdapter = PopularMovieAdapter()
        binding.rvMovies.adapter = movieAdapter

        // Kategori bilgisini al
        val category = arguments?.getString("category") ?: "popular"

        // ViewModel'den filmleri yükle
        when (category) {
            "popular" -> loadPopularMovies()
            "top_rated" -> loadTopRatedMovies()
            "now_playing" -> loadUpcomingMovies()
        }

        // Tıklama listener'ı
        movieAdapter.onMovieClick = { movie ->
            val action = MovieListFragmentDirections.actionMovieListToDetails(movie.id)
            findNavController().navigate(action)
        }

        // Scroll listener'ı ekleyin
        binding.rvMovies.addOnScrollListener(createScrollListener(category))
    }

    private fun loadPopularMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.popularMovies.collect { movies ->
                movieAdapter.movies = movies
            }
        }
        viewModel.loadNextPage("popular")
    }

    private fun loadTopRatedMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.topRatedMovies.collect { movies ->
                movieAdapter.movies = movies
            }
        }
        viewModel.loadNextPage("top_rated")
    }

    private fun loadUpcomingMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.nowPlayingMovies.collect { movies ->
                movieAdapter.movies = movies
            }
        }
        viewModel.loadNextPage("now_playing")
    }

    private fun createScrollListener(category: String): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                // Son öğeye gelindiğinde bir sonraki sayfayı yükle
                if (lastVisibleItemPosition == totalItemCount - 1) {
                    viewModel.loadNextPage(category)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}