package com.bhr.os

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        view.findViewById<View>(R.id.grid4).setOnClickListener {
            prefs.edit().putInt("columns", 4).apply()
            activity?.recreate()
        }
        view.findViewById<View>(R.id.grid5).setOnClickListener {
            prefs.edit().putInt("columns", 5).apply()
            activity?.recreate()
        }

        return view
    }
}
