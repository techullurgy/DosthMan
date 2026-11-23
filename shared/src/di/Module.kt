package com.techullurgy.dosthman.shared.di

import com.techullurgy.dosthman.data.di.dataModule
import com.techullurgy.dosthman.shared.HttpRequestDraftStore
import com.techullurgy.dosthman.shared.screens.workspace.viewmodels.HttpRequestTabViewModel
import com.techullurgy.dosthman.shared.screens.workspace.viewmodels.HttpResponseSectionViewModel
import com.techullurgy.dosthman.shared.screens.workspace.viewmodels.HttpRequestSectionViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    includes(dataModule)

    single<HttpRequestDraftStore> { HttpRequestDraftStore() }

    viewModelOf(::HttpRequestTabViewModel)
    viewModelOf(::HttpRequestSectionViewModel)
    viewModelOf(::HttpResponseSectionViewModel)
}