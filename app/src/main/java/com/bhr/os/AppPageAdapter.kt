package com.bhr.os

import android.content.Context
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.recyclerview.widget.RecyclerView

class AppPageAdapter(
    private val context: Context,
    private val pages: List<List<ResolveInfo>>,
    private val onAppClick: (ResolveInfo) -> Unit
) : RecyclerView.Adapter<AppPageAdapter.PageViewHolder>() {

    inner class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val grid: GridView = itemView as GridView

        fun bind(apps: List<ResolveInfo>) {
            grid.adapter = AppAdapter(context, apps)
            grid.setOnItemClickListener { _, _, pos, _ ->
                onAppClick(apps[pos])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val grid = GridView(context).apply {
            numColumns = 4
            verticalSpacing = 18
            horizontalSpacing = 8
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        return PageViewHolder(grid)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount() = pages.size
}
