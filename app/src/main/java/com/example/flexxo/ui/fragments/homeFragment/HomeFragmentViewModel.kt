package com.example.flexxo.ui.fragments.homeFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.flexxo.data.models.Movie
import com.example.flexxo.data.paging.PopularMoviesPagingSource
import com.example.flexxo.data.paging.TopRatedMoviesPagingSource
import com.example.flexxo.data.paging.UpComingPagingSource
import com.example.flexxo.data.repository.Repository
import kotlinx.coroutines.flow.Flow

class HomeFragmentViewModel : ViewModel() {

    private val repository = Repository()

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