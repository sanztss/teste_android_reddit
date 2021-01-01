package com.fastnews.di

import com.fastnews.data.PostDao
import com.fastnews.repository.PostRepository
import com.fastnews.repository.PostRepositoryImpl
import org.koin.dsl.module

val postRepositoryModule = module {

    fun providePostRepository(dao : PostDao): PostRepository {
        return PostRepositoryImpl(dao)
    }
    single { providePostRepository(get()) }

}