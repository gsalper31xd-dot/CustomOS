package com.bhr.os

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.ceil

class AppsFragment : Fragment() {

    private var startY = 0f
    private lateinit var searchBox: EditText
    private lateinit var allApps: MutableList<android.content.pm.ResolveInfo>
    private lateinit var pager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.page_apps, container, false)

        val pm = requireContext().packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        allApps = pm.queryIntentActivities(intent, 0)
            .filter { it.activityInfo.packageName != requireContext().packageName }
            .sortedBy { it.loadLabel(pm).toString().lowercase() }
            .toMutableList()

        searchBox = view.findViewById(R.id.searchBox)
        val container = view as? ViewGroup

        val prefs = requireContext().getSharedPreferences("bhr_prefs", 0)
        val maxPages = prefs.getInt("app_pages", 1)
        
        setupPager(allApps, container, maxPages)

        container?.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> startY = event.y
                MotionEvent.ACTION_MOVE -> {
                    val diff = event.y - startY
                    if (diff > 100 && searchBox.visibility == View.GONE) {
                        searchBox.visibility = View.VISIBLE
                    }
                }
            }
            false
        }

        searchBox.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && searchBox.text.isEmpty()) {
                searchBox.visibility = View.GONE
            }
        }

        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase()
                val filtered = if (query.isEmpty()) {
                    allApps
                } else {
                    allApps.filter { 
                        it.loadLabel(pm).toString().lowercase().contains(query)
                    }.toMutableList()
                }
                setupPager(filtered, container, maxPages)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        val dock = view.findViewById<LinearLayout>(R.id.dock)
        allApps.take(4).forEach { app ->
            val icon = ImageView(requireContext())
            val size = (56 * resources.displayMetrics.density).toInt()
            val params = LinearLayout.LayoutParams(size, size)
            params.marginStart = 12
            params.marginEnd = 12
            icon.layoutParams = params
            icon.setImageDrawable(app.loadIcon(pm))
            icon.setOnClickListener {
                val launch = Intent(Intent.ACTION_MAIN)
                launch.addCategory(Intent.CATEGORY_LAUNCHER)
                launch.setClassName(app.activityInfo.packageName, app.activityInfo.name)
                launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(launch)
            }
            dock.addView(icon)
        }

        return view
    }

    private fun setupPager(apps: MutableList<android.content.pm.ResolveInfo>, container: ViewGroup?, maxPages: Int) {
        if (::pager.isInitialized) {
            container?.removeView(pager)
        }

        val appsPerPage = 20
        val maxApps = maxPages * appsPerPage
        val limitedApps = apps.take(maxApps).toMutableList()
        
        val pages = ceil(limitedApps.size.toDouble() / appsPerPage).toInt().coerceAtLeast(1)
        val appPages = (0 until pages).map { page ->
            limitedApps.drop(page * appsPerPage).take(appsPerPage).toMutableList()
        }.toMutableList()

        pager = ViewPager2(requireContext())
        pager.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        pager.adapter = AppPageAdapter(requireContext(), appPages) { app ->
            val launch = Intent(Intent.ACTION_MAIN)
            launch.addCategory(Intent.CATEGORY_LAUNCHER)
            launch.setClassName(app.activityInfo.packageName, app.activityInfo.name)
            launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(launch)
        }

        container?.addView(pager, 1)
    }
}
