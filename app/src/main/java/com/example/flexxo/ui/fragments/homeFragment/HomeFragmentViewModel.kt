package com.example.flexxo.ui.fragments.homeFragment

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
import com.example.flexxo.utils.Constants.API_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeFragmentViewModel : ViewModel() {

    private val repository = Repository()
    private val _first15TopRatedMovies = MutableLiveData<Movies>()
    private val _first15PopularMovies = MutableLiveData<Movies>()
    private val _first15UpComingMovies = MutableLiveData<Movies>()
    val first15TopRatedMovies get() = _first15TopRatedMovies
    val first15PopularMovies get() = _first15PopularMovies
    val first15UpcomingMovies get() = _first15UpComingMovies

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

    fun getFirst15TopRatedMovies(){
        viewModelScope.launch {
            _first15TopRatedMovies.postValue(repository.remoteRepository.getTopRatedMovies(API_KEY, 1))
        }
    }

    fun getFirst15PopularMovies(){
        viewModelScope.launch {
            _first15PopularMovies.postValue(repository.remoteRepository.getPopularMovies(API_KEY, 1))
        }
    }

    fun getFirst15UpcomingMovies(){
        viewModelScope.launch {
            _first15UpComingMovies.postValue(repository.remoteRepository.getUpComingMovies(API_KEY, 1))
        }
    }
}