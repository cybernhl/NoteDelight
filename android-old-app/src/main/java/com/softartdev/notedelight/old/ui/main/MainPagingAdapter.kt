package com.softartdev.notedelight.old.ui.main

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.softartdev.notedelight.shared.db.Note
import io.github.aakira.napier.Napier

class MainPagingAdapter(
    private val onNoteClick: (noteId: Long) -> Unit
) : PagingDataAdapter<Note, NotesViewHolder>(NoteDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder =
        NotesViewHolder(parent, onNoteClick)

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) =
        getItem(position)?.let(holder::bind) ?: Napier.d { "item is null" }
}
