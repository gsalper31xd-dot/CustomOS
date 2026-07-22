package com.bhr.os

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.ceil

class AppsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.page_apps, container, false)

        val pm = requireContext().packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val allApps = pm.queryIntentActivities(intent, 0)
            .filter { it.activityInfo.packageName != requireContext().packageName }
            .sortedBy { it.loadLabel(pm).toString().lowercase() }
            .toMutableList()

        val prefs = requireContext().getSharedPreferences("bhr_prefs", 0)
        val maxPages = prefs.getInt("app_pages", 1)
        val appsPerPage = 20
        val maxApps = maxPages * appsPerPage
        val limitedApps = allApps.take(maxApps).toMutableList()

        val pages = ceil(limitedApps.size.toDouble() / appsPerPage).toInt().coerceAtLeast(1)
        val appPages = (0 until pages).map { page ->
            limitedApps.drop(page * appsPerPage).take(appsPerPage).toMutableList()
        }.toMutableList()

        val pager = ViewPager2(requireContext())
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

        val container = view as? ViewGroup
        container?.addView(pager, 0)

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
}
