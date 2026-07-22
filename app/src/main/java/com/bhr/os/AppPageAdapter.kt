package com.bhr.os

import android.content.Context
import android.content.pm.ResolveInfo
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class AppPageAdapter(
    private val context: Context,
    private val pages: MutableList<MutableList<ResolveInfo>>,
    private val onAppClick: (ResolveInfo) -> Unit
) : RecyclerView.Adapter<AppPageAdapter.PageViewHolder>() {

    inner class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val recycler: RecyclerView = itemView as RecyclerView

        fun bind(apps: MutableList<ResolveInfo>) {
            val adapter = AppGridAdapter(context, apps, onAppClick)
            recycler.layoutManager = GridLayoutManager(context, 4)
            recycler.adapter = adapter
            
            val touchHelper = ItemTouchHelper(AppTouchCallback(adapter))
            touchHelper.attachToRecyclerView(recycler)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val recycler = RecyclerView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        return PageViewHolder(recycler)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount() = pages.size
}

class AppGridAdapter(
    private val context: Context,
    private val apps: MutableList<ResolveInfo>,
    private val onAppClick: (ResolveInfo) -> Unit
) : RecyclerView.Adapter<AppGridAdapter.AppViewHolder>() {

    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.appIcon)
        val name: TextView = itemView.findViewById(R.id.appName)

        fun bind(app: ResolveInfo) {
            val pm = context.packageManager
            icon.setImageDrawable(app.loadIcon(pm))
            name.text = app.loadLabel(pm)
            itemView.setOnClickListener { onAppClick(app) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(apps[position])
    }

    override fun getItemCount() = apps.size
}

class AppTouchCallback(private val adapter: AppGridAdapter) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
}
