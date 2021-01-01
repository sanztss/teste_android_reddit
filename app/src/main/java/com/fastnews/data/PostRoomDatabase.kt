package com.fastnews.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fastnews.data.converter.Converters
import com.fastnews.service.model.*

@Database(entities = [PostData::class, Preview::class, PreviewImage::class, PreviewImageSource::class, CommentData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PostRoomDatabase : RoomDatabase() {
    abstract val postDao: PostDao
    abstract val commentDao: CommentDao
}