package com.example.flexxo.data.paging

import androidx.paging.PagingSource
import com.example.flexxo.data.models.MovieDetails
import com.example.flexxo.data.repository.RemoteRepository
import com.example.flexxo.utils.Constants

class PopularMoviesPagingSource(private val remoteRepository: RemoteRepository) :
    PagingSource<Int, MovieDetails>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDetails> {
        return try {
            val page = params.key ?: 1
            val response = remoteRepository.getPopularMovies(Constants.API_KEY, page)
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