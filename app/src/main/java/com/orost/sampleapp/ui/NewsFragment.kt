package com.orost.sampleapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.orost.sampleapp.R
import com.orost.sampleapp.ui.adapter.NewsAdapter
import com.orost.sampleapp.utils.DataState
import com.orost.sampleapp.utils.InfiniteScrollListener
import com.orost.sampleapp.viewmodel.NewsViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_news.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : BaseFragment() {

    private val newsViewModel: NewsViewModel by viewModel()
    private val picasso: Picasso by inject()
    private var newsAdapter = NewsAdapter(picasso)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun initUI(savedInstanceState: Bundle?) {
        news_recycler.apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            adapter = newsAdapter
            setHasFixedSize(true)
            addOnScrollListener(InfiniteScrollListener(linearLayoutManager) {
                newsViewModel.forceFetchNews()
            })
        }

        swipe_container.setOnRefreshListener {
            newsAdapter.news.clear()
            newsViewModel.after = ""
            newsViewModel.forceFetchNews()
        }
        swipe_container.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        )
    }

    override fun subscribeToLiveData() {
        newsViewModel.newsLiveData.observe(this, Observer { news ->
            switchViewsVisibility(news)
            when (news) {
                is DataState.Success -> {
                    newsAdapter.news.addAll(news.data)
                }
            }
        })
    }

    private fun switchViewsVisibility(dataState: DataState<Any>) {
        swipe_container.isRefreshing = dataState is DataState.Loading
        news_recycler.visibility = if (dataState is DataState.Success) View.VISIBLE else View.GONE
        error_text.visibility = if (dataState is DataState.Error) View.VISIBLE else View.GONE
    }
}
