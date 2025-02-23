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
import com.aliaktas.cinematch.adapter.GenreAdapter
import com.aliaktas.cinematch.adapter.MovieAdapter
import com.aliaktas.cinematch.adapter.PopularMovieAdapter
import com.aliaktas.cinematch.adapter.TopRatedMovieAdapter
import com.aliaktas.cinematch.adapter.NowPlayingMovieAdapter
import com.aliaktas.cinematch.data.model.Movie
import com.aliaktas.cinematch.databinding.FragmentHomeBinding
import com.aliaktas.cinematch.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    // Mevcut adapter'lar
    private val popularMovieAdapter = PopularMovieAdapter()
    private val topRatedMovieAdapter = TopRatedMovieAdapter()
    private val nowPlayingMovieAdapter = NowPlayingMovieAdapter()

    // Yeni adapter'lar
    private val genreAdapter = GenreAdapter()
    private val genreMovieAdapter = MovieAdapter() // Film kartları için genel adapter kullanıyoruz

    private var isClickable = true // Tıklama kontrolü için

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        setupClickListeners()
        setupScrollListeners()
    }

    private fun setupRecyclerView() {
        // Mevcut RecyclerView'ların kurulumu
        binding.rvPopularMovies.adapter = popularMovieAdapter
        binding.rvTopRatedMovies.adapter = topRatedMovieAdapter
        binding.rvNowPlayingMovies.adapter = nowPlayingMovieAdapter

        // Yeni RecyclerView'ların kurulumu
        binding.rvGenres.adapter = genreAdapter
        binding.rvGenreMovies.adapter = genreMovieAdapter

        // Yatay kaydırma için LayoutManager'lar zaten XML'de tanımlı
    }

    private fun observeViewModel() {
        // Mevcut film listelerini observe etme
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.popularMovies.collect { movies ->
                popularMovieAdapter.movies = movies
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.topRatedMovies.collect { movies ->
                topRatedMovieAdapter.movies = movies
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.nowPlayingMovies.collect { movies ->
                nowPlayingMovieAdapter.movies = movies
            }
        }

        // Yeni genre ve genre filmleri için observe
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.genres.collect { genres ->
                genreAdapter.genres = genres
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedGenreMovies.collect { movies ->
                genreMovieAdapter.movies = movies
                // Genre filmleri varsa göster, yoksa gizle
                binding.rvGenreMovies.visibility =
                    if (movies.isNotEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setupClickListeners() {
        // Mevcut film tıklama olayları
        popularMovieAdapter.onMovieClick = { movie ->
            handleMovieClick(movie)
        }
        topRatedMovieAdapter.onMovieClick = { movie ->
            handleMovieClick(movie)
        }
        nowPlayingMovieAdapter.onMovieClick = { movie ->
            handleMovieClick(movie)
        }
        genreMovieAdapter.onMovieClick = { movie ->
            handleMovieClick(movie)
        }

        // Genre tıklama olayı
        genreAdapter.onGenreClick = { genre ->
            genreAdapter.selectedGenreId = genre.id
            viewModel.selectGenre(genre.id)
        }

        // See All butonları
        binding.btnSeeAllPopular.setOnClickListener {
            handleSeeAllClick("popular")
        }
        binding.btnSeeAllTopRated.setOnClickListener {
            handleSeeAllClick("top_rated")
        }
        binding.btnSeeAllNowPlaying.setOnClickListener {
            handleSeeAllClick("now_playing")
        }
    }

    private fun handleSeeAllClick(category: String) {
        if (isClickable) {
            isClickable = false
            val action = HomeFragmentDirections.actionHomeToMovieList(category)
            findNavController().navigate(action)
            view?.postDelayed({ isClickable = true }, 300)
        }
    }

    private fun handleMovieClick(movie: Movie) {
        if (isClickable) {
            isClickable = false
            val action = HomeFragmentDirections.actionHomeToDetails(movie.id)
            findNavController().navigate(action)
            view?.postDelayed({ isClickable = true }, 300)
        }
    }

    private fun setupScrollListeners() {
        // Mevcut scroll listener'lar
        binding.rvPopularMovies.addOnScrollListener(createScrollListener("popular"))
        binding.rvTopRatedMovies.addOnScrollListener(createScrollListener("top_rated"))
        binding.rvNowPlayingMovies.addOnScrollListener(createScrollListener("now_playing"))

        // Genre filmleri için scroll listener
        binding.rvGenreMovies.addOnScrollListener(createScrollListener("genre"))
    }

    private fun createScrollListener(category: String): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

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