package com.softartdev.notedelight.shared.presentation.main

import androidx.paging.*
import com.softartdev.notedelight.shared.base.BaseViewModel
import com.softartdev.notedelight.shared.data.NoteUseCase
import com.softartdev.notedelight.shared.db.Note
import com.squareup.sqldelight.android.paging3.QueryPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class MainPagingViewModel(
    private val noteUseCase: NoteUseCase,
) : BaseViewModel<NotePagingResult>() {

    override val loadingResult: NotePagingResult = NotePagingResult.Loading

    init {
        noteUseCase.doOnRelaunchFlow(this::updatePaging)
    }

    fun updatePaging() = launch {
         val pager: Pager<Long, Note> = Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                val sqlDelightPagingDelegate = QueryPagingSource(
                    countQuery = noteUseCase.countQuery(),
                    transacter = noteUseCase.transacter(),
                    dispatcher = Dispatchers.IO,
                    queryProvider = noteUseCase::queryProvider,
                )
                return@Pager CustomPagingSource(sqlDelightPagingDelegate)
            }
        )
        val pagingDataFlow: Flow<PagingData<Note>> = pager.flow.cachedIn(viewModelScope)
        return@launch NotePagingResult.Success(result = pagingDataFlow)
    }

    override fun errorResult(
        throwable: Throwable,
    ): NotePagingResult = when (throwable::class.simpleName?.contains("SQLite")) {
        true -> NotePagingResult.NavMain
        else -> NotePagingResult.Error(throwable.message)
    }

    override fun onCleared() = noteUseCase.doOnRelaunchFlow(null)
}
