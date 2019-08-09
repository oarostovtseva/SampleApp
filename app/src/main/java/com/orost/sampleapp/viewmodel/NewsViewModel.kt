package com.orost.sampleapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orost.sampleapp.api.ApiService
import com.orost.sampleapp.model.RedditNewsData
import com.orost.sampleapp.utils.CoroutineContextProvider
import com.orost.sampleapp.utils.DataState
import kotlinx.coroutines.launch
import timber.log.Timber

class NewsViewModel(private val apiService: ApiService,
                    private val coroutineContextProvider: CoroutineContextProvider
) : BaseViewModel() {

    val newsLiveData = MutableLiveData<DataState<MutableList<RedditNewsData>>>()

    override fun onViewCreated() {
        super.onViewCreated()
        fetchNews()
    }

    private fun fetchNews() {
        viewModelScope.launch(coroutineContextProvider.io) {
            try {
                val request = apiService.getNews()
                val response = request.await()
                val news = response.data.children.map { it.data }.toMutableList()
                newsLiveData.postValue(DataState.Success(news))
            } catch (e: Exception) {
                Timber.e(e, "Error while fetching news")
            }
        }
    }
}
