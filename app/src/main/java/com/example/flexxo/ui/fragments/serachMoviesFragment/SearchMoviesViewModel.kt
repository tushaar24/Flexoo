package com.example.flexxo.ui.fragments.serachMoviesFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flexxo.data.common.models.Movies
import com.example.flexxo.domain.repository.Repository
import com.example.flexxo.utils.Constants.API_KEY
import com.example.flexxo.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMoviesViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private var _movieList: MutableLiveData<Movies> = MutableLiveData()
    val movieList get() = _movieList

    fun searchMovies(query: String) {
        viewModelScope.launch {
            try {
                val result = repository.searchMovies(API_KEY, query)
                when (result) {
                    is NetworkResult.Success -> {
                        _movieList.postValue(result!!.data!!)
                    }

                    else -> {
                    }
                }

            } catch (e: Exception) {
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}