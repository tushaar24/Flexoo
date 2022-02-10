package com.example.flexxo.data.repository.paging

import android.util.Log
import androidx.paging.PagingSource
import com.example.flexxo.data.models.Movie
import com.example.flexxo.data.remote.MoviesService
import com.example.flexxo.utils.Constants.API_KEY

class TopRatedMoviesPagingSource(private val moviesService : MoviesService) : PagingSource<Int, Movie>(){
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try{
            val page = params.key ?: 1
            val response = moviesService.getTopRatedMovies(API_KEY, page).results
            LoadResult.Page(
                response,
                prevKey = if(page == 1) null else page - 1,
                nextKey = if(response.isEmpty()) null  else page + 1
            )
        }catch (e : Exception){
            LoadResult.Error(e)
        }
    }

}