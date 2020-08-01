package com.example.axxessproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.axxessproject.models.ImageItem
import com.example.axxessproject.repository.ImageRepository

/**
 * View Model associated with ImageActivity
 */
class ImageViewModel(application: Application) : AndroidViewModel(application) {
    private val imageRepository: ImageRepository
    fun getImageFromDb(imageId: String?): LiveData<ImageItem> {
        return imageRepository.getImageById(imageId)
    }

    fun saveImageItem(imageItem: ImageItem?) {
        imageRepository.saveImageItem(imageItem)
    }

    init {
        imageRepository = ImageRepository.getInstance(application)
    }
}