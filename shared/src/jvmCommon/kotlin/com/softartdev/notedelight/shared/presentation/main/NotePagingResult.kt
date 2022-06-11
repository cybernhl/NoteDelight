package com.softartdev.notedelight.shared.presentation.main

import androidx.paging.PagingData
import com.softartdev.notedelight.shared.db.Note
import kotlinx.coroutines.flow.Flow

sealed class NotePagingResult {
    object Loading : NotePagingResult()
    data class Success(val result: Flow<PagingData<Note>>) : NotePagingResult()
    object NavMain : NotePagingResult()
    data class Error(val error: String? = null) : NotePagingResult()
}
