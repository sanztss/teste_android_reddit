package com.fastnews.di

import com.fastnews.viewmodel.PostViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val postViewModelModule = module {

    viewModel {
        PostViewModel(repository = get())
    }

}