package com.aliaktas.cinematch.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.aliaktas.cinematch.R
import com.aliaktas.cinematch.databinding.FragmentDetailsBinding
import com.aliaktas.cinematch.viewmodel.DetailsViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailsViewModel by viewModels()
    private var movieId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        movieId = DetailsFragmentArgs.fromBundle(requireArguments()).movieId
//        viewModel.loadMovieDetails(movieId)

        binding.btnFavorite.setOnClickListener {
            viewModel.toggleFavorite()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            // Observe Movie details
            launch {
                viewModel.movie.collect { movie ->
                    movie?.let {
                        binding.apply {
                            tvTitle.text = it.title
                            tvOverview.text = it.overview
                            tvReleaseDate.setText("Release date: ${it.releaseDate}")
                            tvRate.setText("★ ${String.format("%.1f", it.voteAverage)}/10")
                            val posterUrl = "https://image.tmdb.org/t/p/w500${it.posterPath}"
                            Glide.with(requireContext())
                                .load(posterUrl)
                                .centerCrop()
                                .into(ivPoster)
                        }
                    }
                }
            }

            // Observe favorite statue
            launch {
                viewModel.isFavorite(movieId).collect { isFavorite ->
                    binding.btnFavorite.apply {
                        isSelected = isFavorite
                        text = if (isFavorite) "✓ Added" else "Watchlist+"
                        setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                if (isFavorite) R.color.buttonclicked else R.color.buttonnormal
                            )
                        )
                    }
                }
            }
            // Watch trailers on Youtube
            launch {
                viewModel.trailerUrl.collect { url ->
                    binding.buttonFragment.isVisible = url != null
                    binding.buttonFragment.setOnClickListener {
                        url?.let { trailerUrl ->
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
                                startActivity(intent)
                            } catch (e: Exception) {
                                // Go to explorer if there is no Youtube app
                                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
                                startActivity(webIntent)
                            }
                        }
                    }
                }
            }

            launch {
                viewModel.error.collect { errorMessage ->
                    errorMessage?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    }
                }
            }

            launch {
                viewModel.isLoading.collect { isLoading ->
                    binding.progressBar.isVisible = isLoading
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
