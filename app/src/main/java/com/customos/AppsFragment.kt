package com.customos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment

class AppsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.page_apps, container, false)
        val grid = view.findViewById<GridView>(R.id.appGrid)
        val dock = view.findViewById<LinearLayout>(R.id.dock)

        val pm = requireContext().packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val apps = pm.queryIntentActivities(intent, 0)
            .filter { it.activityInfo.packageName != requireContext().packageName }
            .sortedBy { it.loadLabel(pm).toString().lowercase() }

        grid.adapter = AppAdapter(requireContext(), apps)
        grid.setOnItemClickListener { _, _, pos, _ ->
            launchApp(apps[pos])
        }

        val dockApps = apps.take(4)
        dockApps.forEach { app ->
            val icon = ImageView(requireContext())
            val size = (56 * resources.displayMetrics.density).toInt()
            val params = LinearLayout.LayoutParams(size, size)
            params.marginStart = 12
            params.marginEnd = 12
            icon.layoutParams = params
            icon.setImageDrawable(app.loadIcon(pm))
            icon.setOnClickListener { launchApp(app) }
            dock.addView(icon)
        }

        return view
    }

    private fun launchApp(app: android.content.pm.ResolveInfo) {
        val launch = Intent(Intent.ACTION_MAIN)
        launch.addCategory(Intent.CATEGORY_LAUNCHER)
        launch.setClassName(app.activityInfo.packageName, app.activityInfo.name)
        launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(launch)
    }
}
