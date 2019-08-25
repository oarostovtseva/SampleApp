package com.orost.sampleapp.viewmodel

import com.orost.sampleapp.InstantExecutorExtension
import com.orost.sampleapp.api.ApiService
import com.orost.sampleapp.model.RedditChildren
import com.orost.sampleapp.model.RedditData
import com.orost.sampleapp.model.RedditNews
import com.orost.sampleapp.test
import com.orost.sampleapp.utils.DataState
import com.orost.sampleapp.utils.TestContextProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
internal class NewsViewModelTest {

    private val newsChildrens = listOf(RedditChildren(mockk()), RedditChildren(mockk()))
    private val newsData = newsChildrens.map { it.data }.toMutableList()
    private val redditNews = RedditNews(RedditData("", "", newsChildrens))
    private val newsResponse = CompletableDeferred(redditNews)

    private val apiService: ApiService = mockk {
        every { getNewsAsync() } returns newsResponse
    }

    private val viewModel = NewsViewModel(apiService, TestContextProvider())

    @Test
    fun `Success fetching news`() {
        val testObserver = viewModel.newsLiveData.test()

        verify { apiService.getNewsAsync() }
        testObserver.assertValue(DataState.Success(newsData))
    }

    @Test
    fun `Error fetching news`() {
        val apiException = Exception()
        every { apiService.getNewsAsync() } throws apiException

        val testObserver = viewModel.newsLiveData.test()

        verify { apiService.getNewsAsync() }
        testObserver.assertValue(DataState.Error(apiException))
    }
}