package com.example.flexxo.ui.fragments.serachMoviesFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flexxo.data.common.models.MovieDetails
import com.example.flexxo.databinding.FragmentSearchMoviesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchMoviesFragment : Fragment() {

    private var _binding: FragmentSearchMoviesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mAdapter: SearchMoviesAdapter
    private val searchMoviesViewModel: SearchMoviesViewModel by viewModels()
    private var savedQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        binding.svMovies.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchMoviesViewModel.searchMovies(query)
                    binding.progressBar.visibility = View.VISIBLE
                    savedQuery = query
                }
                searchMoviesViewModel.movieList.observe(viewLifecycleOwner) {
                    mAdapter.setData(it.results)
                    binding.progressBar.visibility = View.GONE
                }

                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) {
                    searchMoviesViewModel.searchMovies(query)
                    binding.progressBar.visibility = View.VISIBLE
                    savedQuery = query
                }
                searchMoviesViewModel.movieList.observe(viewLifecycleOwner) {
                    mAdapter.setData(it.results)
                    binding.progressBar.visibility = View.GONE
                }

                return false
            }
        })
    }

    private fun setUpRecyclerView() {

        val onClick: (MovieDetails) -> Unit = {
            val bundle = Bundle()
            bundle.putSerializable("movieDetails", it)
            val direction =
                SearchMoviesFragmentDirections.actionSearchMoviesFragmentToMovieDetailFragment(
                    it
                )
            findNavController().navigate(direction)
        }

        mAdapter = SearchMoviesAdapter(requireContext(), onClick)
        binding.rvSearchMoviesResult.adapter = mAdapter
        binding.rvSearchMoviesResult.layoutManager = LinearLayoutManager(requireContext())
        mAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onResume() {
        super.onResume()

        if (savedQuery.isNotEmpty() && savedQuery.isNotBlank()) {
            binding.progressBar.visibility = View.VISIBLE
        }

        searchMoviesViewModel.searchMovies(savedQuery)
        searchMoviesViewModel.movieList.observe(viewLifecycleOwner) {
            mAdapter.setData(it.results)
            binding.progressBar.visibility = View.GONE
        }
    }

}