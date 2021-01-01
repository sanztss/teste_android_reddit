package com.fastnews.service.model

import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class PostResponse(val data: PostDataChild)

data class PostDataChild(val children: List<PostChildren>)

data class PostChildren(val data: PostData)

@Entity(tableName = "post_table")
@Parcelize
data class PostData(@PrimaryKey(autoGenerate = false) val id: String,
                    val author: String,
                    val thumbnail: String,
                    val name: String,
                    val num_comments: Int,
                    val score: Int,
                    val title: String,
                    val created_utc: Long,
                    val url: String,
                    @Expose
                    @Embedded
                    @Nullable
                    val preview: Preview?) : Parcelable

@Entity(tableName = "preview_table")
@Parcelize
data class Preview(
    @PrimaryKey(autoGenerate = true)
    @Expose
    @SerializedName("id")
    var previewId: Int,
    @SerializedName("images")
    @Expose
    val images: List<PreviewImage>) : Parcelable

@Entity(tableName = "preview_image_table")
@Parcelize
data class PreviewImage(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    val previewImageId: String,
    @SerializedName("source")
    @Expose
    @Embedded
    val source: PreviewImageSource) : Parcelable

@Entity(tableName = "preview_image_source_table")
@Parcelize
data class PreviewImageSource(
    @PrimaryKey(autoGenerate = true)
    @Expose
    @SerializedName("id")
    var previewImageSourceId: Int,
    @SerializedName("url")
    @Expose
    val url: String,
    @SerializedName("width")
    @Expose
    val width: Int,
    @SerializedName("height")
    @Expose
    val height: Int) : Parcelable