package com.example.vesputiapp.di

import com.example.vesputiapp.repositories.GetItemRepository
import com.example.vesputiapp.repositories.remote.ApiInstance
import com.example.vesputiapp.repositories.remote.GetItemService
import com.example.vesputiapp.viewModel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val apiModule = module {
    single { ApiInstance.create() }
}

val itemModule = module {
    viewModel { MainViewModel(get()) }
    single { GetItemRepository(get()) }
    single { GetItemService(get()) }
}