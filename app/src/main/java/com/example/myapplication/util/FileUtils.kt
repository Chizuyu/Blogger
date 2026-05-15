package com.example.myapplication.util

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

fun Uri.toMultipartBody(context: Context, partName: String): MultipartBody.Part? {
    val contentResolver = context.contentResolver
    val type = contentResolver.getType(this)
    val extension = when (type) {
        "image/jpeg" -> ".jpg"
        "image/png" -> ".png"
        else -> ".jpg"
    }

    val tempFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}$extension")

    return try {
        val inputStream = contentResolver.openInputStream(this) ?: return null
        val outputStream = FileOutputStream(tempFile)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        val requestFile = tempFile.asRequestBody(type?.toMediaTypeOrNull())
        MultipartBody.Part.createFormData(partName, tempFile.name, requestFile)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}