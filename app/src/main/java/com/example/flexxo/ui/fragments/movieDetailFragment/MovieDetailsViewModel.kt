package com.example.flexxo.ui.fragments.movieDetailFragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flexxo.data.common.models.MovieCastEntity
import com.example.flexxo.data.common.models.MovieDetails
import com.example.flexxo.data.common.models.Movies
import com.example.flexxo.domain.repository.Repository
import com.example.flexxo.utils.Constants.API_KEY
import com.example.flexxo.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _movieCastList: MutableStateFlow<List<MovieCastEntity>> =
        MutableStateFlow(emptyList())
    val movieCastList get() = _movieCastList.asStateFlow()

    private val _movieDetails: MutableLiveData<MovieDetails> = MutableLiveData()
    val movieDetails get() = _movieDetails
    private val _similarMovies: MutableLiveData<Movies> = MutableLiveData()
    val similarMovies get() = _similarMovies
    private val _recommendedMovies: MutableLiveData<Movies> = MutableLiveData()
    val recommendedMovies get() = _recommendedMovies

    fun getMovieCast(movieId: Int) {
        viewModelScope.launch {
            when (val result = repository.getMovieCredits(movieId, API_KEY)) {
                is NetworkResult.Success -> {
                    _movieCastList.emit(result.data.cast)
                }

                else -> {
                    Log.d("oxoxtushar", "unknown error")
                }
            }
        }
    }

    fun getMovieDetails(
        movieId: Int
    ) {
        viewModelScope.launch {
            when (val result = repository.getMovieDetails(movieId, API_KEY)) {
                is NetworkResult.Success -> {
                    _movieDetails.postValue(result.data!!)
                }

                else -> {
                    Log.d("oxoxtushar", "unknown error")
                }
            }
        }
    }

    fun getSimilarMovies(
        movieId: Int
    ) {
        viewModelScope.launch {
            when (val result = repository.getSimilarMovies(movieId, "aeae0dd396c7be022ed7575b84e62da7")) {
                is NetworkResult.Success -> {
                    _similarMovies.postValue(result.data!!)
                    Log.d("oxoxtushar", "data: ${result.data}")
                }
                is NetworkResult.Failure -> {
                    Log.d("oxoxtushar", "failure: ${result.code}")
                }

                is NetworkResult.Exception -> {
                    Log.d("oxoxtushar", "exception : ${result.exception.message}")
                }

                else -> {
                    Log.d("oxoxtushar", "unknown error")
                }
            }
        }
    }

    fun getRecommendedMovie(
        movieId: Int
    ) {
        viewModelScope.launch {
            when (val result = repository.getRecommendedMovies(movieId, API_KEY)) {
                is NetworkResult.Success -> {
                    _recommendedMovies.postValue(result.data!!)
                }
                is NetworkResult.Failure -> {
                    Log.d("oxoxtushar", "failure: ${result.message}")
                }

                is NetworkResult.Exception -> {
                    Log.d("oxoxtushar", "exception : ${result.exception.message}")
                }

                else -> {
                    Log.d("oxoxtushar", "unknown error")
                }
            }
        }
    }
}