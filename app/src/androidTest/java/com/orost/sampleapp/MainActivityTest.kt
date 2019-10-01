package com.orost.sampleapp

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.orost.sampleapp.RecyclerViewAssertions.Companion.hasItemCount
import com.orost.sampleapp.api.ApiService
import com.orost.sampleapp.model.RedditChildren
import com.orost.sampleapp.model.RedditData
import com.orost.sampleapp.model.RedditNews
import com.orost.sampleapp.model.RedditNewsData
import com.orost.sampleapp.utils.TestContextProvider
import com.orost.sampleapp.viewmodel.NewsViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.CompletableDeferred
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

@RunWith(JUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    internal var activityRule: ActivityTestRule<MainActivity> =
        ActivityTestRule(MainActivity::class.java, false, false)

    private val apiService: ApiService = mockk {
        every { getNewsAsync(any(), any()) } returns CompletableDeferred(getNewsItems())
    }

    private val newsViewModel: NewsViewModel = spyk(NewsViewModel(apiService, TestContextProvider()))

    @Before
    fun setup() {
        loadKoinModules(listOf(module {
            viewModel(override = true) { newsViewModel }
        }))
    }

    @Test
    fun appLaunchesSuccessfully() {
        activityRule.launchActivity(null)
    }

    @Test
    fun onLaunchCheckSuccessState() {
        activityRule.launchActivity(Intent())

        Espresso.onView(withId(R.id.news_recycler)).check(matches(isDisplayed()))
        Espresso.onView(withId(R.id.error_text)).check(matches(not(isDisplayed())))
    }

    @Test
    fun onLaunchCheckNewsItemsDisplay() {
        activityRule.launchActivity(Intent())

        onView(RecyclerViewMatchers.withRecyclerView(R.id.news_recycler).atPosition(0))
            .check(matches(hasDescendant(withText("title1"))))
        onView(RecyclerViewMatchers.withRecyclerView(R.id.news_recycler).atPosition(1))
            .check(matches(hasDescendant(withText("title2"))))
    }

    @Test
    fun countNewsItems() {
        activityRule.launchActivity(Intent())
        onView(withId(R.id.news_recycler)).check(hasItemCount(getNewsItems().data.children.size))
    }

    @Test
    fun onLaunchCheckErrorState() {

        every { apiService.getNewsAsync(any(), any()) } throws Exception()

        activityRule.launchActivity(Intent())

        Espresso.onView(withId(R.id.news_recycler)).check(matches(not(isDisplayed())))
        Espresso.onView(withId(R.id.error_text)).check(matches(isDisplayed()))
    }

    private fun getNewsItems(): RedditNews {
        val dataItem1 = RedditNewsData(
            author = "aithor",
            title = "title1",
            numComments = 1,
            created = 1L,
            thumbnail = "https://images.app.goo.gl/MMg93wPYPAYFw4Fm9",
            url = "https://www.reddit.com/r/funny/comments/dbfsl0/did_i_waste_my_life/"
        )
        val dataItem2 = RedditNewsData(
            author = "aithor2",
            title = "title2",
            numComments = 1,
            created = 1L,
            thumbnail = "https://images.app.goo.gl/MMg93wPYPAYFw4Fm9",
            url = "https://www.reddit.com/r/funny/comments/dbfsl0/did_i_waste_my_life/"
        )
        val newsChildrens = listOf(RedditChildren(dataItem1), RedditChildren(dataItem2))
        return RedditNews(RedditData("item1", "", newsChildrens))
    }
}
