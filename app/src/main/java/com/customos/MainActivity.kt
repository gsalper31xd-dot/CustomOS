package com.customos

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
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
        pager.setPageTransformer { page, position ->
            page.alpha = 1 - kotlin.math.abs(position) * 0.3f
            page.scaleY = 1 - (kotlin.math.abs(position) * 0.08f)
        }

        val dots = listOf(
            findViewById<View>(R.id.dot1),
            findViewById<View>(R.id.dot2),
            findViewById<View>(R.id.dot3)
        )
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                dots.forEachIndexed { i, dot ->
                    dot.setBackgroundResource(if (i == position) R.drawable.dot_active else R.drawable.dot_inactive)
                }
            }
        })
    }

    fun setWallpaper(uri: Uri) {
        try {
            val input = contentResolver.openInputStream(uri)
            val original = BitmapFactory.decodeStream(input)
            input?.close()

            val blurred = BlurUtils.blur(this, original, 18f)

            val file = File(filesDir, "wallpaper_blur.jpg")
            val output = FileOutputStream(file)
            blurred.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, output)
            output.close()

            wallpaperView.setImageBitmap(blurred)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadSavedWallpaper() {
        val file = File(filesDir, "wallpaper_blur.jpg")
        if (file.exists()) {
            wallpaperView.setImageURI(Uri.fromFile(file))
        }
    }
}
