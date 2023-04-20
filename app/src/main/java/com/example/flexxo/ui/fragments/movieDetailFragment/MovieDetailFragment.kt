package com.example.flexxo.ui.fragments.movieDetailFragment

import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.flexxo.R
import com.example.flexxo.data.common.models.MovieDetails
import com.example.flexxo.databinding.FragmentMovieDetailBinding
import com.example.flexxo.ui.fragments.homeFragment.HomeMoviesAdapter
import com.example.flexxo.utils.Constants
import com.example.flexxo.utils.Constants.IMAGE_BACKDROP_BASE_URL
import com.example.flexxo.utils.Constants.IMAGE_POST_BASE_URL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val args: MovieDetailFragmentArgs by navArgs()
    private val movieDetailsViewModel: MovieDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieDetails = args.movieDetails

        Log.d("oxoxtushar", "movieId: ${movieDetails.id}")

        movieDetailsViewModel.getMovieCast(movieDetails.id)

        val posterImageUrl = IMAGE_POST_BASE_URL + movieDetails?.poster_path

        Glide.with(requireContext())
            .load(posterImageUrl)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .thumbnail(0.5f)
            .preload()

        val backDropImageUrl = IMAGE_BACKDROP_BASE_URL + movieDetails?.backdrop_path

        Glide.with(requireContext())
            .load(backDropImageUrl)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .thumbnail(0.5f)
            .preload()

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            movieDetailsViewModel.movieCastList.collect {
                withContext(Dispatchers.Main) {
                    if (_binding != null) {
                        binding.rvCast.adapter = MovieCastAdapter(requireContext(), it)
                        binding.rvCast.layoutManager =
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                    } else {
                    }
                }
            }
        }

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

            Glide.with(requireContext())
                .load(posterImageUrl)
                .into(binding.ivPosterImage)

            Glide.with(requireContext())
                .load(backDropImageUrl)
                .into(binding.ivBackImage)
        }



        movieDetailsViewModel.getRecommendedMovie(movieDetails.id)
        movieDetailsViewModel.getSimilarMovies(movieDetails.id)

        movieDetailsViewModel.similarMovies.observe(viewLifecycleOwner) {
            if(it.results.isNotEmpty()){
                binding.rvSimilarMovies.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.rvSimilarMovies.adapter =
                    HomeMoviesAdapter(getOnMovieItemClicked(), it.results, requireContext())
                binding.shimmerSimilarMovies.stopShimmer()
                binding.shimmerSimilarMovies.visibility = View.GONE
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
            }else{
                binding.rvSimilarMovies.visibility = View.GONE
                binding.tvSimilarMovies.visibility = View.GONE
                binding.shimmerSimilarMovies.visibility = View.GONE
            }

        }

        movieDetailsViewModel.recommendedMovies.observe(viewLifecycleOwner) {
            if(it.results.isNotEmpty()){
                binding.rvRecommendedMovies.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.rvRecommendedMovies.adapter =
                    HomeMoviesAdapter(getOnMovieItemClicked(), it.results, requireContext())
                binding.shimmerRecommendedMovies.stopShimmer()
                binding.shimmerRecommendedMovies.visibility = View.GONE
            }else{
                binding.rvRecommendedMovies.visibility = View.GONE
                binding.tvRecommendedMovies.visibility = View.GONE
                binding.shimmerRecommendedMovies.visibility = View.GONE
            }
        }

    }

    private fun getOnMovieItemClicked(): (MovieDetails) -> Unit {
        val onMovieItemClicked: (MovieDetails) -> Unit = { movie ->
            val bundle = Bundle()
            bundle.putSerializable(Constants.MOVIE_DETAILS, movie)
            val direction =
                MovieDetailFragmentDirections.actionMovieDetailFragmentSelf(movie)
            findNavController().navigate(direction)
        }
        return onMovieItemClicked
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}