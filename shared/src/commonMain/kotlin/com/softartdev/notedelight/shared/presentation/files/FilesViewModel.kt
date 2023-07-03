package com.softartdev.notedelight.shared.presentation.files

import com.softartdev.notedelight.shared.base.BaseViewModel
import com.softartdev.notedelight.shared.files.FileRepo
import kotlinx.coroutines.flow.map

class FilesViewModel(private val fileRepo: FileRepo) : BaseViewModel<FilesResult>() {
    override val loadingResult: FilesResult = FilesResult.Loading

    fun updateFiles() = launch(flow = fileRepo.fileListFlow.map(FilesResult::Success))

    fun onItemClicked(fileName: String) = launchAction {
        fileRepo.onItemClicked(fileName)
    }

    override fun errorResult(throwable: Throwable) = FilesResult.Error(throwable.message)
}