package com.bhr.os

import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.content.Context

object BlurUtils {
    fun blur(context: Context, bitmap: Bitmap, radius: Float = 20f): Bitmap {
        val width = (bitmap.width * 0.4f).toInt().coerceAtLeast(1)
        val height = (bitmap.height * 0.4f).toInt().coerceAtLeast(1)
        val scaled = Bitmap.createScaledBitmap(bitmap, width, height, true)

        return try {
            val rs = RenderScript.create(context)
            val input = Allocation.createFromBitmap(rs, scaled)
            val output = Allocation.createTyped(rs, input.type)
            val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            script.setRadius(radius.coerceIn(0f, 25f))
            script.setInput(input)
            script.forEach(output)
            output.copyTo(scaled)
            rs.destroy()
            Bitmap.createScaledBitmap(scaled, bitmap.width, bitmap.height, true)
        } catch (e: Exception) {
            scaled
        }
    }
}
