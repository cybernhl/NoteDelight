package com.softartdev.notedelight.shared.di

import com.softartdev.notedelight.shared.base.KmmViewModel
import org.koin.core.definition.Definition
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier


actual inline fun <reified T : KmmViewModel> Module.viewModelFactory(
    qualifier: Qualifier?,
    noinline definition: Definition<T>
): KoinDefinition<T> = factory(qualifier, definition)