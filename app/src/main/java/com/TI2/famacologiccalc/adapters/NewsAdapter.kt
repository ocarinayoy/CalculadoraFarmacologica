package com.TI2.famacologiccalc.adapters

import com.TI2.famacologiccalc.R
import com.TI2.famacologiccalc.database.models.News
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class NewsAdapter(
    private val newsList: List<News>
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    // ViewHolder para los ítems
    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.tv_news_title)
        val descriptionTextView: TextView = view.findViewById(R.id.tv_news_description)
        val imageView: ImageView = view.findViewById(R.id.iv_news_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_home, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.titleTextView.text = news.title
        holder.descriptionTextView.text = news.description
        if (news.imageResId != 0) {
            holder.imageView.setImageResource(news.imageResId)
        } else {
            holder.imageView.visibility = View.GONE // Oculta la imagen si no hay
        }
    }

    override fun getItemCount(): Int = newsList.size
}
