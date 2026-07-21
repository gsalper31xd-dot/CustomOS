package com.customos

import android.content.Context
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class AppAdapter(
    private val context: Context,
    private val apps: List<ResolveInfo>
) : BaseAdapter() {

    private val pm = context.packageManager
    private val inflater = LayoutInflater.from(context)
    private val iconCache = HashMap<Int, android.graphics.drawable.Drawable>()

    override fun getCount(): Int = apps.size
    override fun getItem(position: Int): Any = apps[position]
    override fun getItemId(position: Int): Long = position.toLong()

    private class ViewHolder(view: View) {
        val icon: ImageView = view.findViewById(R.id.appIcon)
        val name: TextView = view.findViewById(R.id.appName)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.item_app, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val app = apps[position]
        holder.icon.setImageDrawable(
            iconCache.getOrPut(position) { app.loadIcon(pm) }
        )
        holder.name.text = app.loadLabel(pm)

        return view
    }
}
