package com.example.flexxo.ui.fragments.serachMoviesFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flexxo.data.models.Movies
import com.example.flexxo.data.repository.RemoteRepository
import com.example.flexxo.utils.Constants.API_KEY
import kotlinx.coroutines.launch

class SearchMoviesViewModel : ViewModel() {

    private val remoteRepository = RemoteRepository()

    private var _movieList: MutableLiveData<Movies> = MutableLiveData()
    val movieList get() = _movieList

    private var _isListIsEmpty: MutableLiveData<Boolean> = MutableLiveData()
    val isListIsEmpty get() = _isListIsEmpty

    fun searchMovies(query: String) {
        viewModelScope.launch {
            try {
                _movieList.postValue(remoteRepository.searchMovies(API_KEY, query))
                if (_movieList.value != null) {
                    if (_movieList.value?.results != null) {
                        if (_movieList.value!!.results.isEmpty()) {
                            _isListIsEmpty.postValue(true)
                        }
                    }
                }
            } catch (e: Exception) {
                _isListIsEmpty.postValue(true)
            }
        }
    }
}