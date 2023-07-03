package com.softartdev.notedelight.shared.di

import com.softartdev.notedelight.shared.database.AndroidDbRepo
import com.softartdev.notedelight.shared.database.DatabaseRepo
import com.softartdev.notedelight.shared.files.AndroidFileRepo
import com.softartdev.notedelight.shared.files.FileRepo
import org.koin.core.module.Module
import org.koin.dsl.module

actual val repoModule: Module = module {
    single<DatabaseRepo> { AndroidDbRepo(get()) }
    single<FileRepo> { AndroidFileRepo(context = get()) }
}
