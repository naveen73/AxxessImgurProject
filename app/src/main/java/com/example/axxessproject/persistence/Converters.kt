package com.example.axxessproject.persistence

import androidx.room.TypeConverter
import com.example.axxessproject.models.ImageItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
/**
 * Converters used to store custom objects in room database. This class is provided as converter type
 * to RoomDatabase instance.
 */
object Converters {
    @JvmStatic
    @TypeConverter
    fun fromStringOfImageList(value: String): List<ImageItem.Image> {
        val listType = object : TypeToken<List<ImageItem.Image>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @JvmStatic
    @TypeConverter
    fun fromArrayListOfImage(list: List<ImageItem.Image?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringOfImage(value: String?): ImageItem.Image {
        val imageType = object : TypeToken<ImageItem.Image?>() {}.type
        return Gson().fromJson(value, imageType)
    }

    @TypeConverter
    fun fromImage(image: ImageItem.Image?): String {
        val gson = Gson()
        return gson.toJson(image)
    }
}