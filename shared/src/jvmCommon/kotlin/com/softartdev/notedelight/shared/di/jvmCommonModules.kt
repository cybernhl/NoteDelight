package com.softartdev.notedelight.shared.di

import com.softartdev.notedelight.shared.presentation.main.MainPagingViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val pagingViewModelModule: Module = module {
    viewModelFactory { MainPagingViewModel(get()) }
}
