package com.orost.sampleapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orost.sampleapp.api.ApiService
import com.orost.sampleapp.model.RedditNewsData
import com.orost.sampleapp.utils.CoroutineContextProvider
import com.orost.sampleapp.utils.DataState
import kotlinx.coroutines.launch
import timber.log.Timber

class NewsViewModel(
        private val apiService: ApiService,
        private val coroutineContextProvider: CoroutineContextProvider
) : ViewModel() {

    val newsLiveData by lazy {
        val liveData = MutableLiveData<DataState<MutableList<RedditNewsData>>>()
        fetchNews { liveData.postValue(it) }
        return@lazy liveData
    }

    fun forceFetchNews(){
        fetchNews { newsLiveData.postValue(it) }
    }

    private fun fetchNews(onLoad: (DataState<MutableList<RedditNewsData>>) -> Unit) {
        viewModelScope.launch(coroutineContextProvider.io) {
            onLoad.invoke(DataState.Loading)
            try {
                val request = apiService.getNewsAsync()
                val response = request.await()
                val news = response.data.children.map { it.data }.toMutableList()
                onLoad.invoke(DataState.Success(news))
            } catch (e: Exception) {
                onLoad.invoke(DataState.Error(e))
                Timber.e(e, "Error while fetching news")
            }
        }
    }
}
