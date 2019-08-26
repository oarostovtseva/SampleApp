package com.orost.sampleapp.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orost.sampleapp.R
import com.orost.sampleapp.model.RedditNewsData
import com.squareup.picasso.Picasso
import inflate
import kotlinx.android.synthetic.main.item_news.view.*

internal class NewsAdapter(val picasso: Picasso) : RecyclerView.Adapter<NewsAdapter.AbstractViewHolder>() {

    var news = mutableListOf<RedditNewsData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return if (position == news.size) R.layout.item_progress else R.layout.item_news
    }

    override fun getItemCount() = news.size + 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val view = parent.inflate(viewType)
        return when (viewType) {
            R.layout.item_progress -> ProgressViewHolder(view)
            R.layout.item_news -> ItemViewHolder(view)
            else ->
                throw IllegalArgumentException(
                        "Using not allowed type. " +
                                "You pass $viewType, instead of " +
                                "[${R.layout.item_news}, ${R.layout.item_progress}]"
                )
        }
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        holder.bind()
    }

    open inner class AbstractViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun bind() {}
    }

    inner class ItemViewHolder(view: View) : AbstractViewHolder(view) {
        override fun bind() {
            val item = news[adapterPosition]
            itemView.title.text = item.title
            itemView.author.text = item.author
            itemView.comments.text = itemView.context.resources.getString(R.string.comments_text, item.numComments)
            picasso.load(item.thumbnail).error(R.drawable.ic_empty_picture).into(itemView.icon)
        }
    }

    inner class ProgressViewHolder(view: View) : AbstractViewHolder(view)
}

internal interface ItemHelper {
    fun loadMore()
}
