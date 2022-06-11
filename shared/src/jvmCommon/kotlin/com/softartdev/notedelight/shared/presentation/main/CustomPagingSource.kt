package com.softartdev.notedelight.shared.presentation.main

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.softartdev.notedelight.shared.db.Note

class CustomPagingSource(
    private val sqlDelightPagingDelegate: PagingSource<Long, Note>
) : PagingSource<Long, Note>() {

    override fun getRefreshKey(state: PagingState<Long, Note>): Long? =
        sqlDelightPagingDelegate.getRefreshKey(state)

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Note> = try {
        sqlDelightPagingDelegate.load(params)
    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}
