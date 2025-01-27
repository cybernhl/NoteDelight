package com.softartdev.notedelight.shared.database

import com.softartdev.notedelight.shared.date.DateAdapter
import com.softartdev.notedelight.shared.db.Note
import com.softartdev.notedelight.shared.db.NoteDb
import com.softartdev.notedelight.shared.db.NoteQueries
import app.cash.sqldelight.db.SqlDriver
import kotlinx.datetime.LocalDateTime

fun createQueryWrapper(sqlDriver: SqlDriver): NoteDb {
    val dateColumnAdapter = DateAdapter()
    val noteColumnAdapter = Note.Adapter(dateColumnAdapter, dateColumnAdapter)
    return NoteDb(driver = sqlDriver, noteAdapter = noteColumnAdapter)
}

object TestSchema {

    val firstNote = Note(
        id = 1,
        title = "first title from test schema",
        text = "first text",
        dateCreated = LocalDateTime(2018, 1, 2, 3, 4),
        dateModified = LocalDateTime(2018, 2, 3, 4, 5),
    )
    val secondNote = Note(
        id = 2,
        title = "second title",
        text = "second text",
        dateCreated = LocalDateTime(2017, 1, 2, 3, 4),
        dateModified = LocalDateTime(2017, 2, 3, 4, 5),
    )
    val thirdNote = Note(
        id = 3,
        title = "third title",
        text = "third text",
        dateCreated = LocalDateTime(2016, 1, 2, 3, 4),
        dateModified = LocalDateTime(2016, 2, 3, 4, 5),
    )

    fun insertTestNotes(noteQueries: NoteQueries) =
        sequenceOf(firstNote, secondNote, thirdNote).forEach(noteQueries::insert)
}
