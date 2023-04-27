package com.example.flexxo.ui.fragments.movieDetailFragment

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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _movieCastList: MutableLiveData<List<MovieCastEntity>> =
        MutableLiveData(emptyList())
    val movieCastList get() = _movieCastList
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
                    _movieCastList.postValue(result.data.cast)
                }

                else -> {
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
                    val data = result.data
                    _movieDetails.postValue(data)
                }

                else -> {
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
                    val data = result.data
                    _similarMovies.postValue(data)
                }
                is NetworkResult.Failure -> {
                }

                is NetworkResult.Exception -> {
                }

                else -> {
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
                    val data = result.data
                    _recommendedMovies.postValue(data)
                }
                is NetworkResult.Failure -> {
                }

                is NetworkResult.Exception -> {
                }

                else -> {
                }
            }
        }
    }
}