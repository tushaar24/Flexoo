package com.example.flexxo.ui.fragments.movieViewAll

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.flexxo.data.models.Movie
import com.example.flexxo.data.models.Movies
import com.example.flexxo.data.paging.PopularMoviesPagingSource
import com.example.flexxo.data.paging.TopRatedMoviesPagingSource
import com.example.flexxo.data.paging.UpComingPagingSource
import com.example.flexxo.data.repository.Repository
import com.example.flexxo.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MovieViewAllViewModel : ViewModel() {
    private val repository = Repository()
    private val _movieList = MutableLiveData<Movies>()
    val movieList get() = _movieList

    fun getFirst15TopRatedMovies() {
        viewModelScope.launch {
            _movieList.postValue(
                repository.remoteRepository.getTopRatedMovies(
                    Constants.API_KEY,
                    1
                )
            )
        }
    }

    fun getFirst15PopularMovies() {
        viewModelScope.launch {
            _movieList.postValue(repository.remoteRepository.getPopularMovies(Constants.API_KEY, 1))
        }
    }

    fun getFirst15UpcomingMovies() {
        viewModelScope.launch {
            _movieList.postValue(
                repository.remoteRepository.getUpComingMovies(
                    Constants.API_KEY,
                    1
                )
            )
        }
    }

    fun getTopRatedMovies(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(100)
    ) {
        TopRatedMoviesPagingSource(repository.remoteRepository)
    }.flow.cachedIn(viewModelScope)

    fun getPopularMovies(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(100)
    ) {
        PopularMoviesPagingSource(repository.remoteRepository)
    }.flow.cachedIn(viewModelScope)

    fun getUpComingMovies(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(100)
    ) {
        UpComingPagingSource(repository.remoteRepository)
    }.flow.cachedIn(viewModelScope)
}