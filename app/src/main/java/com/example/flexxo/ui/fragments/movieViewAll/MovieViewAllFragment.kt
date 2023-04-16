package com.example.flexxo.ui.fragments.movieViewAll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flexxo.R
import com.example.flexxo.data.common.models.MovieDetails
import com.example.flexxo.databinding.FragmentMovieViewAllBinding
import com.example.flexxo.ui.fragments.homeFragment.HomeMoviesAdapter
import com.example.flexxo.ui.fragments.homeFragment.MoviesAdapter
import com.example.flexxo.utils.Constants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MovieViewAllFragment : Fragment() {

    private var _binding: FragmentMovieViewAllBinding? = null
    private val binding get() = _binding!!
    private lateinit var mViewModel: MovieViewAllViewModel
    private val args: MovieViewAllFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieViewAllBinding.inflate(inflater, container, false)
        mViewModel = ViewModelProvider(requireActivity()).get(MovieViewAllViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBackBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }


        val layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvMovies.layoutManager = layoutManager
        binding.rvMovies.addItemDecoration(GridSpacingItemDecoration(3, 16, false))

        when (args.type) {
            "latest" -> {
                viewLifecycleOwner.lifecycleScope.launch{
                    mViewModel.getPopularMovies().collectLatest { movies ->
                        val adapter = setAdapter()
                        binding.rvMovies.adapter = adapter
                        adapter.submitData(lifecycle, movies)
                    }
                }
                binding.tvTitle.text = requireContext().getString(R.string.homescreen_latest_movie_title)
            }

            "top rated" -> {
                viewLifecycleOwner.lifecycleScope.launch{
                    mViewModel.getTopRatedMovies().collectLatest { movies ->
                        val adapter = setAdapter()
                        binding.rvMovies.adapter = adapter
                        adapter.submitData(lifecycle, movies)
                    }
                }
                binding.tvTitle.text = requireContext().getString(R.string.homescreen_top_rated_movies_title)
            }

            "upcoming" -> {
                viewLifecycleOwner.lifecycleScope.launch{
                    mViewModel.getUpComingMovies().collectLatest { movies ->
                        val adapter = setAdapter()
                        binding.rvMovies.adapter = adapter
                        adapter.submitData(lifecycle, movies)
                    }
                }
                binding.tvTitle.text = requireContext().getString(R.string.homescreen_upcoming_movies_title)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getOnMovieItemClicked(): (MovieDetails) -> Unit {
        val onMovieItemClicked: (MovieDetails) -> Unit = { movie ->
            val bundle = Bundle()
            bundle.putSerializable(Constants.MOVIE_DETAILS, movie)
            val direction =
                MovieViewAllFragmentDirections.actionMovieViewAllFragmentToMovieDetailFragment(movie)
            findNavController().navigate(direction)
        }
        return onMovieItemClicked
    }

    private fun setAdapter(): MoviesAdapter {
        val onMovieItemClicked = getOnMovieItemClicked()
        val mAdapter = MoviesAdapter(onMovieItemClicked, requireContext(), true)
        mAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        mAdapter.setLastPositionToMinusOne()
        mAdapter.isDataSet.observe(requireActivity()) { dataSetChanged ->
            if (dataSetChanged) {
                setRecyclerViewVisible()
            }
        }
        return mAdapter
    }

    private fun setRecyclerViewVisible() {
        binding.rvMovies.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }
}