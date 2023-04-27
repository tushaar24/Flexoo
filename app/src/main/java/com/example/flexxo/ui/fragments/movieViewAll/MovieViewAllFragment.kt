package com.example.flexxo.ui.fragments.movieViewAll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flexxo.R
import com.example.flexxo.data.common.models.MovieDetails
import com.example.flexxo.databinding.FragmentMovieViewAllBinding
import com.example.flexxo.ui.fragments.homeFragment.MoviesAdapter
import com.example.flexxo.utils.Constants.MOVIE_DETAILS_ARG
import com.example.flexxo.utils.Constants.POPULAR_ARG
import com.example.flexxo.utils.Constants.TOP_RATED_ARG
import com.example.flexxo.utils.Constants.UPCOMING_ARG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieViewAllFragment : Fragment() {

    private var _binding: FragmentMovieViewAllBinding? = null
    private val binding get() = _binding!!
    private val mViewModel: MovieViewAllViewModel by viewModels()
    private val args: MovieViewAllFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieViewAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUI() {

        binding.ivBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        setRecyclerViewBasedOnMovieType()
    }

    private fun setRecyclerViewBasedOnMovieType() {
        when (args.type) {

            POPULAR_ARG -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    mViewModel.getPopularMovies().collectLatest { movies ->
                        setAdapter(movies)
                    }
                }
                binding.tvTitle.text =
                    requireContext().getString(R.string.homescreen_latest_movie_title)
            }

            TOP_RATED_ARG -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    mViewModel.getTopRatedMovies().collectLatest { movies ->
                        setAdapter(movies)
                    }
                }
                binding.tvTitle.text =
                    requireContext().getString(R.string.homescreen_top_rated_movies_title)
            }

            UPCOMING_ARG -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    mViewModel.getUpComingMovies().collectLatest { movies ->
                        setAdapter(movies)
                    }
                }
                binding.tvTitle.text =
                    requireContext().getString(R.string.homescreen_upcoming_movies_title)
            }
        }
    }

    private fun getOnMovieItemClicked(): (MovieDetails) -> Unit {
        val onMovieItemClicked: (MovieDetails) -> Unit = { movie ->
            val bundle = Bundle()
            bundle.putSerializable(MOVIE_DETAILS_ARG, movie)
            val direction =
                MovieViewAllFragmentDirections.actionMovieViewAllFragmentToMovieDetailFragment(movie)
            findNavController().navigate(direction)
        }
        return onMovieItemClicked
    }

    private fun setAdapter(movies: PagingData<MovieDetails>) {
        val layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvMovies.layoutManager = layoutManager
        binding.rvMovies.addItemDecoration(GridSpacingItemDecoration(3, 16, false))
        val onMovieItemClicked = getOnMovieItemClicked()
        val mAdapter = MoviesAdapter(onMovieItemClicked, requireContext(), true)
        mAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        mAdapter.setLastPositionToMinusOne()
        mAdapter.isDataSet.observe(viewLifecycleOwner) { dataSetChanged ->
            if (dataSetChanged) {
                setRecyclerViewVisible()
            }
        }
        binding.rvMovies.adapter = mAdapter
        mAdapter.submitData(lifecycle, movies)
    }

    private fun setRecyclerViewVisible() {
        binding.rvMovies.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }
}