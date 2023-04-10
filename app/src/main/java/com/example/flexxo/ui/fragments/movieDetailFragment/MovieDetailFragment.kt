package com.example.flexxo.ui.fragments.movieDetailFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.flexxo.databinding.FragmentMovieDetailBinding
import com.example.flexxo.utils.Constants.IMAGE_BACKDROP_BASE_URL
import com.example.flexxo.utils.Constants.IMAGE_POST_BASE_URL

class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val args: MovieDetailFragmentArgs by navArgs()

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

        binding.tvMovieTitle.text = movieDetails?.title
        binding.tvOverview.text = movieDetails?.overview
        binding.tvReleaseDate.text = movieDetails?.release_date
        binding.tvPopularity.text = movieDetails?.popularity.toString()
        binding.tvAverageRating.text = movieDetails?.vote_average.toString()
        binding.tvRateCount.text = movieDetails?.vote_count.toString()

        val posterImageUrl = IMAGE_POST_BASE_URL + movieDetails?.poster_path

        Glide.with(requireContext())
            .load(posterImageUrl)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .thumbnail(0.5f)
            .into(binding.ivPosterImage)

        val backDropImageUrl = IMAGE_BACKDROP_BASE_URL + movieDetails?.backdrop_path

        Glide.with(requireContext())
            .load(backDropImageUrl)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .thumbnail(0.5f)
            .into(binding.ivBackImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}