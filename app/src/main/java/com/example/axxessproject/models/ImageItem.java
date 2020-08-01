package com.example.axxessproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This is the entity which gets stored in local room db. This is also model that gets parsed from
 * network response
 */
@Entity(tableName = "images")
public class ImageItem implements Parcelable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose()
    private String id;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose()
    private String title;

    @ColumnInfo(name = "imagelist")
    @SerializedName("images")
    @Expose()
    private List<Image> images;

    @ColumnInfo(name = "comment")
    private String comment = "";

    public ImageItem(){}


    protected ImageItem(Parcel in) {
        id = in.readString();
        title = in.readString();
        images = in.createTypedArrayList(Image.CREATOR);
        comment = in.readString();
    }

    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
        @Override
        public ImageItem createFromParcel(Parcel in) {
            return new ImageItem(in);
        }

        @Override
        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getImageUrl() {
        if (images != null) {
            return images.get(0).imageLink;
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeTypedList(images);
        parcel.writeString(comment);
    }

    public static class Image implements Parcelable {
        @SerializedName("link")
        @Expose()
        private String imageLink;

        protected Image(Parcel in) {
            imageLink = in.readString();
        }

        public static final Creator<Image> CREATOR = new Creator<Image>() {
            @Override
            public Image createFromParcel(Parcel in) {
                return new Image(in);
            }

            @Override
            public Image[] newArray(int size) {
                return new Image[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(imageLink);
        }
    }
}
