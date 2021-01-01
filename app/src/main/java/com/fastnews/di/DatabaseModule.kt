package com.fastnews.di

import android.app.Application
import androidx.room.Room
import com.fastnews.data.CommentDao
import com.fastnews.data.PostDao
import com.fastnews.data.PostRoomDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    fun provideDatabase(application: Application): PostRoomDatabase {
       return Room.databaseBuilder(application, PostRoomDatabase::class.java, "post_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun providePostDao(database: PostRoomDatabase): PostDao {
        return database.postDao
    }

    fun provideCommentDao(database: PostRoomDatabase): CommentDao {
        return database.commentDao
    }

    single { provideDatabase(androidApplication()) }
    single { providePostDao(get()) }
    single { provideCommentDao(get()) }


}
