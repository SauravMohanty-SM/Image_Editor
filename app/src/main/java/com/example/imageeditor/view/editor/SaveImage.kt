package com.example.imageeditor.view.editor

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.*

fun saveImage(context: Context, bitmap: Bitmap) {
    val storageDirectory= Environment.getExternalStorageDirectory().toString()
    val file = File(storageDirectory, "Edited_Image.jpg")
    try {
        val stream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}
