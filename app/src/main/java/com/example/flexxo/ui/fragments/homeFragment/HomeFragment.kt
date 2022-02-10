package com.example.flexxo.ui.fragments.homeFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flexxo.data.models.Movie
import com.example.flexxo.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private enum class Buttons(val id: Int) {
    POPULAR_BUTTON(1),
    UP_COMING_BUTTON(2),
    TOP_RATED_BUTTON(3);

    companion object {
        fun create(x: Int): Buttons {
            return when (x) {
                1 -> POPULAR_BUTTON
                2 -> UP_COMING_BUTTON
                3 -> TOP_RATED_BUTTON
                -1 -> TOP_RATED_BUTTON
                else -> throw IllegalArgumentException("$x is not a valid id for Buttons")
            }
        }
    }
}

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAdapter: MoviesAdapter
    private val mViewModel: HomeFragmentViewModel by viewModels()
    private var buttonState = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.visibility = View.VISIBLE


        val onClick: (Movie) -> Unit = {
            val bundle = Bundle()
            bundle.putSerializable("movieDetails", it)
            val direction =
                HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment().setMovieDetails(it)
            findNavController().navigate(direction)
        }

        mAdapter = MoviesAdapter(onClick, requireContext())
        mAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.btnPopular.setOnClickListener {
            if (!it.isSelected) {
                binding.progressBar.visibility = View.VISIBLE
                setButtonInFocus(Buttons.POPULAR_BUTTON.id)
                getPopularMovies()
                handleLoadingState()
            }
        }

        binding.btnTopRated.setOnClickListener {
            if (!it.isSelected) {
                binding.progressBar.visibility = View.VISIBLE
                setButtonInFocus(Buttons.TOP_RATED_BUTTON.id)
                getTopRatedMovies()
                handleLoadingState()
            }
        }

        binding.btnUpComing.setOnClickListener {
            if (!it.isSelected) {
                binding.progressBar.visibility = View.VISIBLE
                setButtonInFocus(Buttons.UP_COMING_BUTTON.id)
                getUpComingMovies()
            }
        }
        binding.svMovies.setOnClickListener {
            val directions = HomeFragmentDirections.actionHomeFragmentToSearchMoviesFragment()
            findNavController().navigate(directions)
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.rvMovies.adapter = mAdapter
        binding.rvMovies.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setButtonInFocus(button: Int) {

        buttonState = button

        when (Buttons.create(button)) {
            Buttons.TOP_RATED_BUTTON -> {
                binding.btnUpComing.isSelected = false
                binding.btnPopular.isSelected = false
                binding.btnTopRated.isSelected = true
            }

            Buttons.UP_COMING_BUTTON -> {
                binding.btnUpComing.isSelected = true
                binding.btnPopular.isSelected = false
                binding.btnTopRated.isSelected = false
            }

            Buttons.POPULAR_BUTTON -> {
                binding.btnUpComing.isSelected = false
                binding.btnPopular.isSelected = true
                binding.btnTopRated.isSelected = false
            }
        }
    }

    private fun getTopRatedMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.getTopRatedMovies().collectLatest { pagingData ->
                mAdapter.setLastPositionToMinusOne()
                mAdapter.submitData(pagingData)
            }
        }
    }

    private fun getPopularMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.getPopularMovies().collectLatest { pagingData ->
                mAdapter.setLastPositionToMinusOne()
                mAdapter.submitData(pagingData)
            }
        }
    }

    private fun getUpComingMovies() {
        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.getUpComingMovies().collectLatest { pagingData ->
                mAdapter.setLastPositionToMinusOne()
                mAdapter.submitData(pagingData)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        setButtonInFocus(buttonState)

        when (Buttons.create(buttonState)) {
            Buttons.TOP_RATED_BUTTON -> {
                getTopRatedMovies()
                handleLoadingState()
            }

            Buttons.UP_COMING_BUTTON -> {
                getUpComingMovies()
                handleLoadingState()
            }

            Buttons.POPULAR_BUTTON -> {
                getPopularMovies()
                handleLoadingState()
            }
        }
    }

    fun handleLoadingState(){
        viewLifecycleOwner.lifecycleScope.launch {
            mAdapter.loadStateFlow.collectLatest {
                if(it.refresh is LoadState.NotLoading){
                    binding.progressBar.visibility = View.GONE
                    binding.rvMovies.visibility = View.VISIBLE
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}