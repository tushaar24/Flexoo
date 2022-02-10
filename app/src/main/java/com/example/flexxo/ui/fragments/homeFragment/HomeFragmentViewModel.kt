package com.example.flexxo.ui.fragments.homeFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.flexxo.data.models.Movie
import com.example.flexxo.data.repository.paging.PopularMoviesPagingSource
import com.example.flexxo.data.repository.paging.TopRatedMoviesPagingSource
import com.example.flexxo.data.repository.paging.UpComingPagingSource
import com.example.flexxo.data.repository.remote.RemoteRepository
import kotlinx.coroutines.flow.Flow

class HomeFragmentViewModel : ViewModel() {

    private val remoteRepository = RemoteRepository()

    fun getTopRatedMovies(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(10)
    ) {
        TopRatedMoviesPagingSource(remoteRepository.movieService)
    }.flow.cachedIn(viewModelScope)

    fun getPopularMovies(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(5)
    ) {
        PopularMoviesPagingSource(remoteRepository.movieService)
    }.flow.cachedIn(viewModelScope)

    fun getUpComingMovies(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(40)
    ) {
        UpComingPagingSource(remoteRepository.movieService)
    }.flow.cachedIn(viewModelScope)


}