package com.example.flexxo.data.paging

import androidx.paging.PagingSource
import com.example.flexxo.data.models.Movie
import com.example.flexxo.data.repository.RemoteRepository
import com.example.flexxo.utils.Constants

class UpComingPagingSource(private val remoteRepository: RemoteRepository) :
    PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val response = remoteRepository.getUpComingMovies(Constants.API_KEY, page).results
            LoadResult.Page(
                response,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}