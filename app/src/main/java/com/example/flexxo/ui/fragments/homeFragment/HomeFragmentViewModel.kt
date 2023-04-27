package com.example.flexxo.ui.fragments.homeFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flexxo.data.common.models.Movies
import com.example.flexxo.databinding.FragmentHomeV2Binding
import com.example.flexxo.domain.repository.Repository
import com.example.flexxo.utils.Constants.API_KEY
import com.example.flexxo.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _first15TopRatedMovies = MutableLiveData<Movies?>()
    private val _first15PopularMovies = MutableLiveData<Movies?>()
    private val _first15UpComingMovies = MutableLiveData<Movies?>()
    val first15TopRatedMovies get() = _first15TopRatedMovies
    val first15PopularMovies get() = _first15PopularMovies
    val first15UpcomingMovies get() = _first15UpComingMovies
    private var _binding: FragmentHomeV2Binding? = null
    val binding get() = _binding

    fun getFirst15TopRatedMovies() {
        viewModelScope.launch {
            when (val result = repository.getTopRatedMovies(API_KEY, 1)) {
                is NetworkResult.Success -> {
                    _first15TopRatedMovies.postValue(result.data)
                }

                else -> {
                }
            }
        }
    }

    fun getFirst15PopularMovies(){
        viewModelScope.launch {

            when (val result = repository.getPopularMovies(API_KEY, 1)) {
                is NetworkResult.Success -> {
                    _first15PopularMovies.postValue(result.data)
                }

                else -> {
                }
            }
        }
    }

    fun getFirst15UpcomingMovies(){
        viewModelScope.launch {
            when (val result = repository.getUpComingMovies(API_KEY, 1)) {
                is NetworkResult.Success -> {
                    _first15UpComingMovies.postValue(result.data)
                }

                else -> {
                }
            }

        }
    }

    fun setBinding(binding: FragmentHomeV2Binding) {
        _binding = binding
    }
}