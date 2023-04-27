package com.example.flexxo.ui.fragments.movieDetailFragment

import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.flexxo.R
import com.example.flexxo.data.common.models.MovieDetails
import com.example.flexxo.databinding.FragmentMovieDetailBinding
import com.example.flexxo.ui.fragments.homeFragment.HomeMoviesAdapter
import com.example.flexxo.utils.Constants.IMAGE_BACKDROP_BASE_URL
import com.example.flexxo.utils.Constants.IMAGE_POST_BASE_URL
import com.example.flexxo.utils.Constants.MOVIE_DETAILS_ARG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val movieDetailsViewModel: MovieDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: MovieDetailFragmentArgs by navArgs()
        val movieDetails = args.movieDetails
        setUI(movieDetails)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUI(movieDetails: MovieDetails) {
        binding.shimmerRecommendedMovies.startShimmer()
        binding.shimmerSimilarMovies.startShimmer()
        makeNetworkCallsAndObserveData(movieDetails)
    }

    private fun makeNetworkCallsAndObserveData(movieDetails: MovieDetails) {
        fetchCastAndObserve(movieDetails)
        fetchMovieDetails(movieDetails)
        fetchRecommendedMovies(movieDetails)
        fetchSimilarMovies(movieDetails)
    }

    private fun fetchCastAndObserve(movieDetails: MovieDetails) {

        movieDetailsViewModel.getMovieCast(movieDetails.id)

        movieDetailsViewModel.movieCastList.observe(viewLifecycleOwner) {
            binding.rvCast.adapter = MovieCastAdapter(requireContext(), it)
            binding.rvCast.layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
        }
    }

    private fun fetchRecommendedMovies(movieDetails: MovieDetails) {

        movieDetailsViewModel.getRecommendedMovie(movieDetails.id)

        movieDetailsViewModel.recommendedMovies.observe(viewLifecycleOwner) {
            binding.shimmerRecommendedMovies.stopShimmer()
            binding.shimmerRecommendedMovies.visibility = View.GONE
            if (it.results.isNotEmpty()) {
                binding.rvRecommendedMovies.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.rvRecommendedMovies.adapter =
                    HomeMoviesAdapter(getOnMovieItemClicked(), it.results, requireContext())
            } else {
                binding.rvRecommendedMovies.visibility = View.GONE
                binding.tvRecommendedMovies.visibility = View.GONE
            }
        }
    }

    private fun fetchSimilarMovies(movieDetails: MovieDetails) {

        movieDetailsViewModel.getSimilarMovies(movieDetails.id)

        movieDetailsViewModel.similarMovies.observe(viewLifecycleOwner) {
            binding.shimmerSimilarMovies.stopShimmer()
            binding.shimmerSimilarMovies.visibility = View.GONE
            if (it.results.isNotEmpty()) {
                binding.rvSimilarMovies.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.rvSimilarMovies.adapter =
                    HomeMoviesAdapter(getOnMovieItemClicked(), it.results, requireContext())
                TransitionManager.beginDelayedTransition(binding.movieDetailsConstraintLayout)
                val constrainSet = ConstraintSet()
                constrainSet.clone(binding.movieDetailsConstraintLayout)
                constrainSet.connect(
                    binding.tvSimilarMovies.id,
                    ConstraintSet.TOP,
                    binding.rvSimilarMovies.id,
                    ConstraintSet.BOTTOM
                )
                constrainSet.applyTo(binding.movieDetailsConstraintLayout)
            } else {
                binding.rvSimilarMovies.visibility = View.GONE
                binding.tvSimilarMovies.visibility = View.GONE
            }
        }
    }

    private fun fetchMovieDetails(movieDetails: MovieDetails) {

        movieDetailsViewModel.getMovieDetails(movieDetails.id)

        movieDetailsViewModel.movieDetails.observe(viewLifecycleOwner) { movie ->
            binding.progressBar.visibility = View.GONE
            binding.group1.visibility = View.VISIBLE
            binding.group2.visibility = View.VISIBLE
            binding.tvRating.text = String.format("%.1f", movie.vote_average)
            binding.tvReleaseDate.text = requireContext().getString(
                R.string.movie_release_date,
                movie.status,
                movie.release_date
            )
            binding.tvMovieTitle.text = movie.title
            binding.tvOverview.text = movie.overview

            val posterImageUrl = IMAGE_POST_BASE_URL + movieDetails.poster_path

            Glide.with(requireContext())
                .load(posterImageUrl)
                .into(binding.ivPosterImage)

            val backDropImageUrl = IMAGE_BACKDROP_BASE_URL + movieDetails.backdrop_path

            Glide.with(requireContext())
                .load(backDropImageUrl)
                .into(binding.ivBackImage)
        }
    }

    private fun getOnMovieItemClicked(): (MovieDetails) -> Unit {
        val onMovieItemClicked: (MovieDetails) -> Unit = { movie ->
            val bundle = Bundle()
            bundle.putSerializable(MOVIE_DETAILS_ARG, movie)
            val direction =
                MovieDetailFragmentDirections.actionMovieDetailFragmentSelf(movie)
            findNavController().navigate(direction)
        }
        return onMovieItemClicked
    }
}