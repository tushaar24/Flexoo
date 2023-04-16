package com.example.flexxo.ui.fragments.movieDetailFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.flexxo.R
import com.example.flexxo.databinding.FragmentMovieDetailBinding
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

        val navBackStackEntry = findNavController().getBackStackEntry(R.id.movieDetailFragment)
//            movieDetailsViewModel =
//                ViewModelProvider(requireActivity())[MovieDetailsViewModel::class.java]

        val movieDetails = args.movieDetails

        movieDetailsViewModel.getMovieCast(movieDetails.id)
        binding.tvMovieTitle.text = movieDetails?.title
        binding.tvOverview.text = movieDetails?.overview

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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
    }
}