package com.fastnews.data.converter

import androidx.room.TypeConverter
import com.fastnews.service.model.PreviewImage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {

    @TypeConverter
    fun fromPreviewImageList(countryLang: List<PreviewImage?>?): String? {
        val type = object : TypeToken<List<PreviewImage?>?>() {}.type
        return Gson().toJson(countryLang, type)
    }

    @TypeConverter
    fun toPreviewImageList(countryLangString: String?): List<PreviewImage>? {
        val type = object : TypeToken<List<PreviewImage?>?>() {}.type
        return Gson().fromJson<List<PreviewImage>>(countryLangString, type)
    }
}