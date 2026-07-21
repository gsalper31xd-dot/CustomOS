package com.customos

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateClock()
        setupAppDrawer()
    }

    private fun updateClock() {
        val clockView = findViewById<TextView>(R.id.clock)
        val dateView = findViewById<TextView>(R.id.date)
        val handler = android.os.Handler(mainLooper)
        val runnable = object : Runnable {
            override fun run() {
                val now = Date()
                clockView.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now)
                dateView.text = SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(now)
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }

    private fun setupAppDrawer() {
        val grid = findViewById<GridView>(R.id.appGrid)
        val pm = packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val apps = pm.queryIntentActivities(intent, 0)
            .filter { it.activityInfo.packageName != packageName }
            .sortedBy { it.loadLabel(pm).toString().lowercase() }

        grid.adapter = AppAdapter(this, apps)
        grid.setOnItemClickListener { _, _, pos, _ ->
            val app = apps[pos]
            val launch = Intent(Intent.ACTION_MAIN)
            launch.addCategory(Intent.CATEGORY_LAUNCHER)
            launch.setClassName(app.activityInfo.packageName, app.activityInfo.name)
            launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(launch)
        }
    }
}
