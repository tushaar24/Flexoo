package com.example.flexxo.ui.fragments.homeFragment

import android.icu.lang.UCharacter.IndicPositionalCategory.BOTTOM
import android.icu.lang.UCharacter.IndicPositionalCategory.TOP
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flexxo.data.models.Movie
import com.example.flexxo.databinding.FragmentHomeV2Binding
import com.example.flexxo.utils.Constants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragmentV2 : Fragment() {

    private var _binding: FragmentHomeV2Binding? = null
    private val binding get() = _binding!!
    private val mViewModel: HomeFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeV2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            shimmerLatestMovies.startShimmer()
            shimmerTopMovies.startShimmer()
            shimmerUpComingMovies.startShimmer()
        }

        binding.rvUpComingMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvTopRatedMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvLatestMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

//        getPopularMovies()
//        getTopRatedMovies()
//        getUpComingMovies()

        mViewModel.getFirst15PopularMovies()
        mViewModel.getFirst15TopRatedMovies()
        mViewModel.getFirst15UpcomingMovies()

        binding.svMovies.setOnClickListener {
            val directions = HomeFragmentV2Directions.actionHomeFragmentV22ToSearchMoviesFragment()
            findNavController().navigate(directions)
        }

        setObservers()
    }

    private fun setObservers() {
        mViewModel.first15PopularMovies.observe(requireActivity()) { movies ->
            val listOfPopularMovies = mutableListOf<Movie>()
            listOfPopularMovies.addAll(movies.results.subList(0, 16))
            binding.rvLatestMovies.adapter =
                HomeMoviesAdapter(getOnMovieItemClicked(), listOfPopularMovies, requireContext(), "latest", getOnViewMoreClicked())
            binding.shimmerLatestMovies.stopShimmer()
            binding.shimmerLatestMovies.visibility = View.GONE
        }

        mViewModel.first15TopRatedMovies.observe(requireActivity()) { movies ->
            val listOfTopRatedMovies = mutableListOf<Movie>()
            listOfTopRatedMovies.addAll(movies.results.subList(0, 16))
            binding.rvTopRatedMovies.adapter =
                HomeMoviesAdapter(getOnMovieItemClicked(), listOfTopRatedMovies, requireContext(), "top rated", getOnViewMoreClicked())
            binding.shimmerTopMovies.stopShimmer()
            binding.shimmerTopMovies.visibility = View.GONE
            TransitionManager.beginDelayedTransition(binding.clRoot)
            val constrainSet = ConstraintSet()
            constrainSet.clone(binding.clRoot)
            constrainSet.connect(
                binding.tvTopRatedMovies.id,
                ConstraintSet.TOP,
                binding.rvLatestMovies.id,
                ConstraintSet.BOTTOM
            )
            constrainSet.applyTo(binding.clRoot)
        }

        mViewModel.first15UpcomingMovies.observe(requireActivity()) { movies ->
            val listOfUpComingMovies = mutableListOf<Movie>()
            listOfUpComingMovies.addAll(movies.results.subList(0, 16))
            binding.rvUpComingMovies.adapter =
                HomeMoviesAdapter(getOnMovieItemClicked(), listOfUpComingMovies, requireContext(), "upcoming", getOnViewMoreClicked())
            binding.shimmerUpComingMovies.stopShimmer()
            binding.shimmerUpComingMovies.visibility = View.GONE
            TransitionManager.beginDelayedTransition(binding.clRoot)
            val constrainSet = ConstraintSet()
            constrainSet.clone(binding.clRoot)
            constrainSet.connect(
                binding.tvUpComingMovies.id,
                ConstraintSet.TOP,
                binding.rvTopRatedMovies.id,
                ConstraintSet.BOTTOM
            )
            constrainSet.applyTo(binding.clRoot)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun getOnMovieItemClicked(): (Movie) -> Unit {
        val onMovieItemClicked: (Movie) -> Unit = { movie ->
            val bundle = Bundle()
            bundle.putSerializable(Constants.MOVIE_DETAILS, movie)
            val direction =
                HomeFragmentV2Directions.actionHomeFragmentV22ToMovieDetailFragment()
                    .setMovieDetails(movie)
            findNavController().navigate(direction)
        }
        return onMovieItemClicked
    }

    private fun getOnViewMoreClicked(): (String) -> Unit {
        val onViewMoreClicked: (String) -> Unit = {type ->
            val directions = HomeFragmentV2Directions.actionHomeFragmentV22ToMovieViewAllFragment(type)
            findNavController().navigate(directions)
        }
        return onViewMoreClicked
    }

}