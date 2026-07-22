package com.bhr.os

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.page_settings, container, false)
        val prefs = requireContext().getSharedPreferences("bhr_prefs", 0)

        view.findViewById<View>(R.id.themeCream).setOnClickListener {
            prefs.edit().putString("theme", "cream").apply()
            activity?.recreate()
        }
        view.findViewById<View>(R.id.themeDark).setOnClickListener {
            prefs.edit().putString("theme", "dark").apply()
            activity?.recreate()
        }
        view.findViewById<View>(R.id.themeBlue).setOnClickListener {
            prefs.edit().putString("theme", "blue").apply()
            activity?.recreate()
        }

        val pageCountView = view.findViewById<TextView>(R.id.pageCount)
        val addBtn = view.findViewById<Button>(R.id.addPage)
        val removeBtn = view.findViewById<Button>(R.id.removePage)

        fun updatePageCount() {
            val pages = prefs.getInt("app_pages", 1)
            pageCountView.text = "Toplam: $pages sayfa"
            removeBtn.isEnabled = pages > 1
        }

        addBtn.setOnClickListener {
            val pages = prefs.getInt("app_pages", 1)
            prefs.edit().putInt("app_pages", pages + 1).apply()
            updatePageCount()
        }

        removeBtn.setOnClickListener {
            val pages = prefs.getInt("app_pages", 1)
            if (pages > 1) {
                prefs.edit().putInt("app_pages", pages - 1).apply()
                updatePageCount()
            }
        }

        updatePageCount()
        return view
    }
}
