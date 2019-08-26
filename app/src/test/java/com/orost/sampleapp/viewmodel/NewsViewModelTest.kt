package com.orost.sampleapp.viewmodel

import com.orost.sampleapp.InstantExecutorExtension
import com.orost.sampleapp.api.ApiService
import com.orost.sampleapp.model.RedditChildren
import com.orost.sampleapp.model.RedditData
import com.orost.sampleapp.model.RedditNews
import com.orost.sampleapp.model.RedditNewsData
import com.orost.sampleapp.test
import com.orost.sampleapp.utils.DataState
import com.orost.sampleapp.utils.MAX_ITEMS
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
    private val newsChildrensSecondPage = listOf(RedditChildren(mockk()), RedditChildren(mockk()))
    private val newsFirstPageData = newsChildrens.map { it.data }.toMutableList()
    private val newsSecondPageData = newsChildrensSecondPage.map { it.data }.toMutableList()

    private val mergedList by lazy{
        val list = mutableListOf<RedditNewsData>()
        list.addAll(newsFirstPageData)
        list.addAll(newsSecondPageData)
        return@lazy list
    }

    private val redditNewsFirstPage = RedditNews(RedditData("item1", "", newsChildrens))
    private val redditNewsSecondPage = RedditNews(RedditData("item2", "", newsChildrensSecondPage))
    private val newsResponseFirstPage = CompletableDeferred(redditNewsFirstPage)
    private val newsResponseSecondPage = CompletableDeferred(redditNewsSecondPage)

    private val apiService: ApiService = mockk {
        every { getNewsAsync("", MAX_ITEMS) } returns newsResponseFirstPage
        every { getNewsAsync("item1", MAX_ITEMS) } returns newsResponseSecondPage
    }

    private var viewModel = NewsViewModel(apiService, TestContextProvider())

    @Test
    fun `Success fetching first page of news`() {
        val testObserver = viewModel.newsLiveData.test()

        verify { apiService.getNewsAsync("", MAX_ITEMS) }
        testObserver.assertValue(DataState.Success(newsFirstPageData))
    }

    @Test
    fun `Success fetching second page of news`() {
        val testObserver = viewModel.newsLiveData.test()

        verify { apiService.getNewsAsync("", MAX_ITEMS) }
        testObserver.assertValue(DataState.Success(newsFirstPageData))

        viewModel.forceFetchNews()

        verify { apiService.getNewsAsync("item1", MAX_ITEMS) }
        testObserver.assertValueHistory(
                DataState.Success(newsFirstPageData),
                DataState.Loading,
                DataState.Success(mergedList)
        )
        testObserver.assertValue { value ->
            (value is DataState.Success)
                    && value.data.size == newsFirstPageData.size * 2
        }
    }

    @Test
    fun `Error fetching first page of news`() {
        val apiException = Exception()
        every { apiService.getNewsAsync("", MAX_ITEMS) } throws apiException

        val testObserver = viewModel.newsLiveData.test()

        verify { apiService.getNewsAsync("", MAX_ITEMS) }
        testObserver.assertValue(DataState.Error(apiException))
    }

    @Test
    fun `Error fetching second page of news`() {

        val apiException = Exception()
        every { apiService.getNewsAsync("item1", MAX_ITEMS) } throws apiException

        val testObserver = viewModel.newsLiveData.test()

        verify { apiService.getNewsAsync("", MAX_ITEMS) }
        testObserver.assertValue(DataState.Success(newsFirstPageData))

        viewModel.forceFetchNews()

        verify { apiService.getNewsAsync("item1", MAX_ITEMS) }
        testObserver.assertValueHistory(
            DataState.Success(newsFirstPageData),
            DataState.Loading,
            DataState.Error(apiException)
        )
        testObserver.assertValue { it is DataState.Error }
    }
}