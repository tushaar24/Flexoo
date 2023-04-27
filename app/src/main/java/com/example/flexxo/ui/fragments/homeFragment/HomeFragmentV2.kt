package com.example.flexxo.ui.fragments.homeFragment

import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flexxo.data.common.models.MovieDetails
import com.example.flexxo.databinding.FragmentHomeV2Binding
import com.example.flexxo.utils.Constants.MOVIE_DETAILS_ARG
import com.example.flexxo.utils.Constants.POPULAR_ARG
import com.example.flexxo.utils.Constants.TOP_RATED_ARG
import com.example.flexxo.utils.Constants.UPCOMING_ARG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragmentV2 : Fragment() {

    private var _binding: FragmentHomeV2Binding? = null
    private val binding get() = _binding!!
    private val mHomeFragmentViewModel: HomeFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        /*
            Navigation Component Library calls onCreateView again when coming back to a fragment in stack.
            This will make the recyclerviews to recreate. Therefore saving the binding in the viewModel.
        */

        _binding = FragmentHomeV2Binding.inflate(inflater, container, false)
        return if (isBindingCached()) {
            mHomeFragmentViewModel.binding!!.root
        } else {
            mHomeFragmentViewModel.setBinding(binding)
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        makeNetworkCallsAndObserve()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun makeNetworkCallsAndObserve() {
        fetchUpcomingMoviesAndObserve()
        fetchPopularMoviesAndObserve()
        fetchTopRatedMoviesAndObserve()
    }

    private fun fetchTopRatedMoviesAndObserve() {

        mHomeFragmentViewModel.getFirst15TopRatedMovies()

        mHomeFragmentViewModel.first15TopRatedMovies.observe(viewLifecycleOwner) { movies ->
            val listOfTopRatedMovieDetails = mutableListOf<MovieDetails>()
            listOfTopRatedMovieDetails.addAll(movies!!.results.subList(0, 16))
            binding.rvTopRatedMovies.setItemViewCacheSize(0)
            binding.rvTopRatedMovies.adapter =
                HomeMoviesAdapter(
                    getOnMovieItemClicked(),
                    listOfTopRatedMovieDetails,
                    requireContext(),
                    TOP_RATED_ARG,
                    getOnViewMoreClicked()
                )
            binding.shimmerTopMovies.stopShimmer()
            binding.shimmerTopMovies.visibility = View.GONE
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

    private fun fetchUpcomingMoviesAndObserve() {

        mHomeFragmentViewModel.getFirst15UpcomingMovies()

        mHomeFragmentViewModel.first15UpcomingMovies.observe(viewLifecycleOwner) { movies ->
            val listOfUpComingMovieDetails = mutableListOf<MovieDetails>()
            listOfUpComingMovieDetails.addAll(movies!!.results.subList(0, 16))
            binding.rvUpComingMovies.adapter =
                HomeMoviesAdapter(
                    getOnMovieItemClicked(),
                    listOfUpComingMovieDetails,
                    requireContext(),
                    UPCOMING_ARG,
                    getOnViewMoreClicked()
                )
            binding.shimmerUpComingMovies.stopShimmer()
            binding.shimmerUpComingMovies.visibility = View.GONE
        }
    }

    private fun fetchPopularMoviesAndObserve() {

        mHomeFragmentViewModel.getFirst15PopularMovies()

        mHomeFragmentViewModel.first15PopularMovies.observe(viewLifecycleOwner) { movies ->
            val listOfPopularMovieDetails = mutableListOf<MovieDetails>()
            listOfPopularMovieDetails.addAll(movies!!.results.subList(0, 16))
            binding.rvLatestMovies.adapter =
                HomeMoviesAdapter(
                    getOnMovieItemClicked(),
                    listOfPopularMovieDetails,
                    requireContext(),
                    POPULAR_ARG,
                    getOnViewMoreClicked()
                )
            binding.shimmerLatestMovies.stopShimmer()
            binding.shimmerLatestMovies.visibility = View.GONE
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
    }

    private fun setUI() {
        binding.apply {
            shimmerLatestMovies.startShimmer()
            shimmerTopMovies.startShimmer()
            shimmerUpComingMovies.startShimmer()
            rvUpComingMovies.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvTopRatedMovies.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvLatestMovies.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            svMovies.setOnClickListener {
                val directions =
                    HomeFragmentV2Directions.actionHomeFragmentV22ToSearchMoviesFragment()
                findNavController().navigate(directions)
            }
        }
    }

    private fun isBindingCached(): Boolean {
        return mHomeFragmentViewModel.binding?.root != null
    }

    private fun getOnMovieItemClicked(): (MovieDetails) -> Unit {
        val onMovieItemClicked: (MovieDetails) -> Unit = { movie ->
            val bundle = Bundle()
            bundle.putSerializable(MOVIE_DETAILS_ARG, movie)
            val direction =
                HomeFragmentV2Directions.actionHomeFragmentV22ToMovieDetailFragment(movie)
            findNavController().navigate(direction)
        }
        return onMovieItemClicked
    }

    private fun getOnViewMoreClicked(): (String) -> Unit {
        val onViewMoreClicked: (String) -> Unit = { type ->
            val directions =
                HomeFragmentV2Directions.actionHomeFragmentV22ToMovieViewAllFragment(type)
            findNavController().navigate(directions)
        }
        return onViewMoreClicked
    }
}