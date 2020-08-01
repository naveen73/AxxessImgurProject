package com.example.axxessproject.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.axxessproject.models.ImageItem

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImageItem(item: ImageItem?)

    @Query("SELECT * FROM images WHERE id = :id")
    fun getImage(id: String?): LiveData<ImageItem?>?
}