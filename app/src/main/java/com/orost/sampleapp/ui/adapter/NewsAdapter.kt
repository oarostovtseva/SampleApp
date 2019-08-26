package com.orost.sampleapp.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orost.sampleapp.R
import com.orost.sampleapp.model.RedditNewsData
import com.squareup.picasso.Picasso
import inflate
import kotlinx.android.synthetic.main.item_news.view.*

internal class NewsAdapter(val picasso: Picasso) : RecyclerView.Adapter<NewsAdapter.ItemViewHolder>() {

    var news = mutableListOf<RedditNewsData>()
        set(value) {
            val prevSize = field.size
            val newSize = prevSize + value.size
            field = value
            notifyItemRangeInserted(prevSize, newSize)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(news[position])
    }

    override fun getItemCount() = news.size

    inner class ItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_news)) {
        fun bind(item: RedditNewsData) {
            itemView.title.text = item.title
            itemView.author.text = item.author
            itemView.comments.text = itemView.context.resources.getString(R.string.comments_text, item.numComments)
            picasso.load(item.thumbnail).error(R.drawable.ic_empty_picture).into(itemView.icon)
        }
    }
}
