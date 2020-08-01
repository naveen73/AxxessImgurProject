package com.example.axxessproject.network;

import com.example.axxessproject.models.ImageItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Retrofit parses search response to this class and is returned from it.
 */

public class ImageSearchResponse {
    @SerializedName("data")
    @Expose()
    private List<ImageItem> imageItems;

    @SerializedName("success")
    @Expose()
    private boolean success;

    @SerializedName("status")
    @Expose()
    private int status;

    public List<ImageItem> getImageItems() {
        return imageItems;
    }

    public void setImageItems(List<ImageItem> imageItems) {
        this.imageItems = imageItems;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
