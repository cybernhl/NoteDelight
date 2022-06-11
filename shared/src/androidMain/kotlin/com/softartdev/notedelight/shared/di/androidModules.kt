package com.softartdev.notedelight.shared.di

import com.softartdev.notedelight.shared.database.AndroidDbRepo
import com.softartdev.notedelight.shared.database.DatabaseRepo
import com.softartdev.notedelight.shared.presentation.main.MainPagingViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

actual val repoModule: Module = module {
    single<DatabaseRepo> { AndroidDbRepo(get()) }
}

val pagingViewModelModule: Module = module {
    viewModelFactory { MainPagingViewModel(get()) }
}