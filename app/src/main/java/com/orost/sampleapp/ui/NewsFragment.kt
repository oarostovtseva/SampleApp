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
import com.orost.sampleapp.viewmodel.NewsViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_news.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : BaseFragment() {

    private val newsViewModel: NewsViewModel by viewModel()
    private val picasso: Picasso by inject()
    private var adapter = NewsAdapter(picasso)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            newsViewModel.onViewCreated()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initUI(savedInstanceState: Bundle?) {
        news_recycler.adapter = adapter
        news_recycler.layoutManager = LinearLayoutManager(context)
        news_recycler.setHasFixedSize(true)
    }

    override fun subscribeToLiveData() {
        newsViewModel.newsLiveData.observe(this, Observer {
            when (it) {
                is DataState.Success -> {
                    adapter.news = it.data
                    progress_bar.visibility = View.GONE
                }
            }
        })
    }
}
