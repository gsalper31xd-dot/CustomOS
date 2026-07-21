package com.bhr.os

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class ClockFragment : Fragment() {

    private var handler: android.os.Handler? = null
    private var runnable: Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.page_clock, container, false)

        val clockView = view.findViewById<TextView>(R.id.clock)
        val dateView = view.findViewById<TextView>(R.id.date)
        handler = android.os.Handler(requireActivity().mainLooper)
        runnable = object : Runnable {
            override fun run() {
                val now = Date()
                clockView.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now)
                dateView.text = SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(now)
                handler?.postDelayed(this, 60000)
            }
        }
        handler?.post(runnable!!)

        view.findViewById<TextView>(R.id.changeWallpaper).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1001)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        runnable?.let { handler?.removeCallbacks(it) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && data?.data != null) {
            (activity as? MainActivity)?.setWallpaper(data.data!!)
        }
    }
}
