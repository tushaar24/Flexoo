package com.example.flexxo.ui.fragments.homeFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var upComingMoviesAdapter: MoviesAdapter? = null
    private var popularMoviesAdapter: MoviesAdapter? = null
    private var topMoviesAdapter: MoviesAdapter? = null
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

        binding.rvUpComingMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvTopRatedMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvLatestMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        getPopularMovies()
        getTopRatedMovies()
        getUpComingMovies()

        binding.svMovies.setOnClickListener {
            val directions = HomeFragmentV2Directions.actionHomeFragmentV22ToSearchMoviesFragment()
            findNavController().navigate(directions)
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

    private fun getPopularMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.getPopularMovies().collectLatest { pagingData ->
                popularMoviesAdapter = setAdapter()
                binding.rvLatestMovies.adapter = popularMoviesAdapter
                popularMoviesAdapter?.submitData(lifecycle, pagingData)
            }
        }
    }

    private fun getTopRatedMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.getTopRatedMovies().collectLatest { pagingData ->
                topMoviesAdapter = setAdapter()
                binding.rvTopRatedMovies.adapter = topMoviesAdapter
                topMoviesAdapter?.submitData(lifecycle, pagingData)
            }
        }
    }

    private fun getUpComingMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.getUpComingMovies().collectLatest { pagingData ->
                upComingMoviesAdapter = setAdapter()
                binding.rvUpComingMovies.adapter = upComingMoviesAdapter
                upComingMoviesAdapter?.submitData(lifecycle, pagingData)

            }
        }
    }

    private fun setAdapter(): MoviesAdapter {
        val onMovieItemClicked = getOnMovieItemClicked()
        return MoviesAdapter(onMovieItemClicked, requireContext(), true)
    }

}