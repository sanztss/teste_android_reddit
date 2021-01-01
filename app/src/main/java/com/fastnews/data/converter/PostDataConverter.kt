package com.fastnews.data.converter

import androidx.room.TypeConverter
import com.fastnews.service.model.PreviewImage
import com.google.gson.Gson


class PostDataConverter {

    @TypeConverter
    fun listToJson(value: List<PreviewImage>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<PreviewImage>::class.java).toList()
}