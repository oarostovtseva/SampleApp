package com.orost.sampleapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orost.sampleapp.api.ApiService
import com.orost.sampleapp.model.RedditNewsData
import com.orost.sampleapp.utils.CoroutineContextProvider
import com.orost.sampleapp.utils.DataState
import com.orost.sampleapp.utils.MAX_ITEMS
import kotlinx.coroutines.launch
import timber.log.Timber

class NewsViewModel(
        private val apiService: ApiService,
        private val coroutineContextProvider: CoroutineContextProvider
) : ViewModel() {

    var after: String = ""

    val newsLiveData by lazy {
        val liveData = MutableLiveData<DataState<MutableList<RedditNewsData>>>()
        fetchNews { liveData.postValue(it) }
        return@lazy liveData
    }

    private var newsList = mutableListOf<RedditNewsData>()

    fun forceFetchNews() {
        fetchNews { newsLiveData.postValue(it) }
    }

    private fun fetchNews(onLoad: (DataState<MutableList<RedditNewsData>>) -> Unit) {
        viewModelScope.launch(coroutineContextProvider.io) {
            onLoad.invoke(DataState.Loading)
            try {
                val request = apiService.getNewsAsync(after, MAX_ITEMS)
                val response = request.await()
                after = response.data.after
                val news = response.data.children.map { it.data }.toMutableList()
                newsList.addAll(news)
                onLoad.invoke(DataState.Success(newsList.toMutableList()))
            } catch (e: Exception) {
                onLoad.invoke(DataState.Error(e))
                Timber.e(e, "Error while fetching news")
            }
        }
    }
}
