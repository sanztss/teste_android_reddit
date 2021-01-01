package com.fastnews.di

import com.fastnews.data.CommentDao
import com.fastnews.repository.CommentRepository
import com.fastnews.repository.CommentRepositoryImpl
import org.koin.dsl.module

val commentRepositoryModule = module {

    fun provideCommentRepository(dao : CommentDao): CommentRepository {
        return CommentRepositoryImpl(dao)
    }
    single { provideCommentRepository(get()) }

}