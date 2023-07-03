package com.softartdev.notedelight.shared.di

import com.softartdev.notedelight.shared.database.DatabaseRepo
import com.softartdev.notedelight.shared.database.JdbcDbRepo
import com.softartdev.notedelight.shared.files.FileRepo
import com.softartdev.notedelight.shared.files.JvmFileRepo
import org.koin.core.module.Module
import org.koin.dsl.module

actual val repoModule: Module = module {
    single<DatabaseRepo> { JdbcDbRepo() }
    single<FileRepo> { JvmFileRepo() }
}
