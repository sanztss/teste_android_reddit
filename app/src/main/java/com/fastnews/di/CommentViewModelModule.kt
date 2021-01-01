package com.fastnews.di

import com.fastnews.viewmodel.CommentViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val commentViewModelModule = module {

    viewModel {
        CommentViewModel(repository = get())
    }

}