package com.example.flexxo.data.remote.paging.sources

import androidx.paging.PagingSource
import com.example.flexxo.data.common.models.MovieDetails
import com.example.flexxo.domain.repository.Repository
import com.example.flexxo.utils.Constants
import com.example.flexxo.utils.NetworkResult

class UpComingPagingSource(
    private val repository: Repository
) : PagingSource<Int, MovieDetails>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDetails> {
        return try {
            val page = params.key ?: 1
            return when (
                val response = repository.getUpComingMovies(Constants.API_KEY, page)
            ) {
                is NetworkResult.Success -> {
                    val data = response.data.results
                    LoadResult.Page(
                        data,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (data.isEmpty()) null else page + 1
                    )
                }

                is NetworkResult.Failure -> {
                    val failureMessage = response.message
                    LoadResult.Error(Exception(failureMessage))
                }

                is NetworkResult.Exception -> {
                    val exception = response.exception
                    LoadResult.Error(exception)
                }

                else -> {
                    LoadResult.Error(Exception("Something went wrong"))
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}