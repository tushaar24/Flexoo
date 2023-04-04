//package com.example.flexxo.ui.fragments.homeFragment
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.lifecycleScope
//import androidx.navigation.fragment.findNavController
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.flexxo.data.models.MovieDetails
//import com.example.flexxo.databinding.FragmentHomeBinding
//import com.example.flexxo.utils.Constants.MOVIE_DETAILS
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.launch
//
//class HomeFragment : Fragment() {
//
//    private var _binding: FragmentHomeBinding? = null
//    private val binding get() = _binding!!
//    private var upComingMoviesAdapter: MoviesAdapter? = null
//    private var popularMoviesAdapter: MoviesAdapter? = null
//    private var topMoviesAdapter: MoviesAdapter? = null
//    private val mViewModel: HomeFragmentViewModel by viewModels()
//    private var selectedButtonId = -1
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.rvMovies.layoutManager = LinearLayoutManager(requireContext())
//
//        binding.btnPopular.setOnClickListener {
//            handleOnclickButton(Buttons.POPULAR_BUTTON.id, it)
//        }
//
//        binding.btnTopRated.setOnClickListener {
//            handleOnclickButton(Buttons.TOP_RATED_BUTTON.id, it)
//        }
//
//        binding.btnUpComing.setOnClickListener {
//            handleOnclickButton(Buttons.UP_COMING_BUTTON.id, it)
//        }
//
//        binding.svMovies.setOnClickListener {
//            val directions = HomeFragmentDirections.actionHomeFragmentToSearchMoviesFragment()
//            findNavController().navigate(directions)
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        setButtonInFocus(selectedButtonId)
//        getMovies(selectedButtonId)
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    private fun getMovies(buttonId: Int) {
//        when (Buttons.create(buttonId)) {
//            Buttons.POPULAR_BUTTON -> {
//                getPopularMovies()
//            }
//
//            Buttons.TOP_RATED_BUTTON -> {
//                getTopRatedMovies()
//            }
//
//            Buttons.UP_COMING_BUTTON -> {
//                getUpComingMovies()
//            }
//        }
//    }
//
//    private fun getOnMovieItemClicked(): (MovieDetails) -> Unit {
//        val onMovieItemClicked: (MovieDetails) -> Unit = { movie ->
//            val bundle = Bundle()
//            bundle.putSerializable(MOVIE_DETAILS, movie)
//            val direction =
//                HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment()
//                    .setMovieDetails(movie)
//            findNavController().navigate(direction)
//        }
//        return onMovieItemClicked
//    }
//
//    private fun getPopularMovies() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            mViewModel.getPopularMovies().collectLatest { pagingData ->
//                popularMoviesAdapter = setAdapter()
//                binding.rvMovies.adapter = popularMoviesAdapter
//                popularMoviesAdapter?.submitData(lifecycle, pagingData)
//            }
//        }
//    }
//
//    private fun getTopRatedMovies() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            mViewModel.getTopRatedMovies().collectLatest { pagingData ->
//                topMoviesAdapter = setAdapter()
//                binding.rvMovies.adapter = topMoviesAdapter
//                topMoviesAdapter?.submitData(lifecycle, pagingData)
//            }
//        }
//    }
//
//
//    private fun getUpComingMovies() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            mViewModel.getUpComingMovies().collectLatest { pagingData ->
//                upComingMoviesAdapter = setAdapter()
//                binding.rvMovies.adapter = upComingMoviesAdapter
//                upComingMoviesAdapter?.submitData(lifecycle, pagingData)
//
//            }
//        }
//    }
//
//    private fun handleOnclickButton(buttonId: Int, view: View) {
//        if (!view.isSelected) {
//            binding.progressBar.visibility = View.VISIBLE
//            setButtonInFocus(buttonId)
//            getMovies(buttonId)
//        }
//    }
//
//    private fun setAdapter(): MoviesAdapter {
//        val onMovieItemClicked = getOnMovieItemClicked()
//        val mAdapter = MoviesAdapter(onMovieItemClicked, requireContext(), false)
//        mAdapter.stateRestorationPolicy =
//            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
//        mAdapter.setLastPositionToMinusOne()
//        mAdapter.isDataSet.observe(requireActivity()) { dataSetChanged ->
//            if (dataSetChanged) {
//                setRecyclerViewVisible()
//            }
//        }
//        return mAdapter
//    }
//
//    private fun setButtonInFocus(buttonId: Int) {
//
//        selectedButtonId = buttonId
//
//        when (Buttons.create(buttonId)) {
//            Buttons.TOP_RATED_BUTTON -> {
//                binding.btnUpComing.isSelected = false
//                binding.btnPopular.isSelected = false
//                binding.btnTopRated.isSelected = true
//            }
//
//            Buttons.UP_COMING_BUTTON -> {
//                binding.btnUpComing.isSelected = true
//                binding.btnPopular.isSelected = false
//                binding.btnTopRated.isSelected = false
//            }
//
//            Buttons.POPULAR_BUTTON -> {
//                binding.btnUpComing.isSelected = false
//                binding.btnPopular.isSelected = true
//                binding.btnTopRated.isSelected = false
//            }
//        }
//    }
//
//    private fun setRecyclerViewVisible() {
//        binding.rvMovies.visibility = View.VISIBLE
//        binding.progressBar.visibility = View.GONE
//    }
//
//}
//
//private enum class Buttons(val id: Int) {
//    POPULAR_BUTTON(1),
//    UP_COMING_BUTTON(2),
//    TOP_RATED_BUTTON(3);
//
//    companion object {
//        fun create(buttonId: Int): Buttons {
//            return when (buttonId) {
//                1 -> POPULAR_BUTTON
//                2 -> UP_COMING_BUTTON
//                3 -> TOP_RATED_BUTTON
//                -1 -> TOP_RATED_BUTTON
//                else -> throw IllegalArgumentException("$buttonId is not a valid id for Buttons")
//            }
//        }
//    }
//}