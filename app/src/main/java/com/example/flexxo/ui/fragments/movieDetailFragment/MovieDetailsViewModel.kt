package com.example.flexxo.ui.fragments.movieDetailFragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flexxo.data.common.models.MovieCastEntity
import com.example.flexxo.domain.repository.Repository
import com.example.flexxo.utils.Constants.API_KEY
import com.example.flexxo.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    init {
        Log.d("oxoxtushar", "created")
    }

    private val _movieCastList: MutableStateFlow<List<MovieCastEntity>> =
        MutableStateFlow(emptyList())
    val movieCastList get() = _movieCastList.asStateFlow()

    fun getMovieCast(movieId: Int) {
        viewModelScope.launch {
            when (val result = repository.getMovieCredits(movieId, API_KEY)) {
                is NetworkResult.Success -> {
                    _movieCastList.emit(result.data.cast)
                }
                else -> {
                    Log.d("oxoxtusahr", "error")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}