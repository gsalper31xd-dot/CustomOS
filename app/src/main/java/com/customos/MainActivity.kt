package com.customos

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var wallpaperView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wallpaperView = findViewById(R.id.wallpaper)
        loadSavedWallpaper()

        val pager = findViewById<ViewPager2>(R.id.pager)
        pager.adapter = ScreenPagerAdapter(this)
        pager.setCurrentItem(0, false)

        val dot1 = findViewById<View>(R.id.dot1)
        val dot2 = findViewById<View>(R.id.dot2)
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                dot1.setBackgroundResource(if (position == 0) R.drawable.dot_active else R.drawable.dot_inactive)
                dot2.setBackgroundResource(if (position == 1) R.drawable.dot_active else R.drawable.dot_inactive)
            }
        })
    }

    fun setWallpaper(uri: Uri) {
        try {
            val input = contentResolver.openInputStream(uri)
            val file = File(filesDir, "wallpaper.jpg")
            val output = FileOutputStream(file)
            input?.copyTo(output)
            input?.close()
            output.close()
            wallpaperView.setImageURI(Uri.fromFile(file))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadSavedWallpaper() {
        val file = File(filesDir, "wallpaper.jpg")
        if (file.exists()) {
            wallpaperView.setImageURI(Uri.fromFile(file))
        }
    }
}
