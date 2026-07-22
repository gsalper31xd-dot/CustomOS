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

        view.findViewById<View>(R.id.themeCream).setOnClickListener {
            val prefs = requireContext().getSharedPreferences("bhr_prefs", 0)
            prefs.edit().putString("theme", "cream").apply()
            activity?.recreate()
        }
        view.findViewById<View>(R.id.themeDark).setOnClickListener {
            val prefs = requireContext().getSharedPreferences("bhr_prefs", 0)
            prefs.edit().putString("theme", "dark").apply()
            activity?.recreate()
        }
        view.findViewById<View>(R.id.themeBlue).setOnClickListener {
            val prefs = requireContext().getSharedPreferences("bhr_prefs", 0)
            prefs.edit().putString("theme", "blue").apply()
            activity?.recreate()
        }

        return view
    }
}
