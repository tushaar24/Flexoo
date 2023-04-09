package com.example.flexxo.ui.fragments.movieViewAll

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.flexxo.data.common.models.MovieDetails
import com.example.flexxo.data.common.models.Movies
import com.example.flexxo.data.remote.paging.sources.PopularMoviesPagingSource
import com.example.flexxo.data.remote.paging.sources.TopRatedMoviesPagingSource
import com.example.flexxo.data.remote.paging.sources.UpComingPagingSource
import com.example.flexxo.domain.repository.Repository
import com.example.flexxo.utils.Constants
import com.example.flexxo.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewAllViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _movieList = MutableLiveData<Movies?>()
    val movieList get() = _movieList

    fun getFirst15TopRatedMovies() {
        viewModelScope.launch {
            val result = repository.getTopRatedMovies(
                Constants.API_KEY,
                1
            )
            when (result) {
                is NetworkResult.Success -> {
                    _movieList.postValue(
                        result.data
                    )
                }

                else -> {

                }
            }

        }
    }

    fun getFirst15PopularMovies() {
        viewModelScope.launch {
            val result = repository.getPopularMovies(
                Constants.API_KEY,
                1
            )
            when (result) {
                is NetworkResult.Success -> {
                    _movieList.postValue(
                        result.data
                    )
                }

                else -> {

                }
            }
        }
    }

    fun getFirst15UpcomingMovies() {
        viewModelScope.launch {
            val result = repository.getUpComingMovies(
                Constants.API_KEY,
                1
            )
            when (result) {
                is NetworkResult.Success -> {
                    _movieList.postValue(
                        result.data
                    )
                }

                else -> {

                }
            }
        }
    }

    fun getTopRatedMovies(): Flow<PagingData<MovieDetails>> = Pager(
        config = PagingConfig(100)
    ) {
        TopRatedMoviesPagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    fun getPopularMovies(): Flow<PagingData<MovieDetails>> = Pager(
        config = PagingConfig(100)
    ) {
        PopularMoviesPagingSource(repository)
    }.flow.cachedIn(viewModelScope)

    fun getUpComingMovies(): Flow<PagingData<MovieDetails>> = Pager(
        config = PagingConfig(100)
    ) {
        UpComingPagingSource(repository)
    }.flow.cachedIn(viewModelScope)
}