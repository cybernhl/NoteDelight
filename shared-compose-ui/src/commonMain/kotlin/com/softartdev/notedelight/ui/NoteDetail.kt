@file:OptIn(ExperimentalMaterial3Api::class)

package com.softartdev.notedelight.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softartdev.mr.contextLocalized
import com.softartdev.notedelight.MR
import com.softartdev.notedelight.RootComponent
import com.softartdev.notedelight.shared.presentation.note.NoteResult
import com.softartdev.notedelight.shared.presentation.note.NoteViewModel
import com.softartdev.notedelight.ui.dialog.showDelete
import com.softartdev.notedelight.ui.dialog.showEditTitle
import com.softartdev.notedelight.ui.dialog.showError
import com.softartdev.notedelight.ui.dialog.showSaveChanges
import com.softartdev.theme.material3.PreferableMaterialTheme
import com.softartdev.theme.pref.DialogHolder
import com.softartdev.theme.pref.PreferableMaterialTheme.themePrefs
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch

@Composable
fun NoteDetail(
    noteViewModel: NoteViewModel,
    noteId: Long,
    backWrapper: RootComponent.BackWrapper,
    navBack: () -> Unit,
) {
    val noteResultState: State<NoteResult> = noteViewModel.resultStateFlow.collectAsState()
    DisposableEffect(noteId) {
        when (noteId) {
            0L -> noteViewModel.createNote()
            else -> noteViewModel.loadNote(noteId)
        }
        onDispose(noteViewModel::onCleared)
    }
    val titleState: MutableState<String> = remember { mutableStateOf("") }
    val textState: MutableState<String> = remember { mutableStateOf("") }

    backWrapper.handler = { noteViewModel.checkSaveChange(titleState.value, textState.value) }
    val dialogHolder: DialogHolder = themePrefs.dialogHolder

    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    when (val noteResult: NoteResult = noteResultState.value) {
        is NoteResult.Loading,
        is NoteResult.Created -> Unit
        is NoteResult.Loaded -> {
            titleState.value = noteResult.result.title
            textState.value = noteResult.result.text
        }
        is NoteResult.Saved -> coroutineScope.launch {
            titleState.value = noteResult.title
            val noteSaved = MR.strings.note_saved.contextLocalized() + ": " + noteResult.title
            snackbarHostState.showSnackbar(noteSaved)
        }
        is NoteResult.NavEditTitle -> dialogHolder.showEditTitle(noteResult.noteId)
        is NoteResult.TitleUpdated -> {
            titleState.value = noteResult.title
        }
        is NoteResult.Empty -> coroutineScope.launch {
            snackbarHostState.showSnackbar(MR.strings.note_empty.contextLocalized())
        }
        is NoteResult.Deleted -> coroutineScope.launch {
            dialogHolder.dismissDialog()
            navBack()
            snackbarHostState.showSnackbar(MR.strings.note_deleted.contextLocalized())
        }
        is NoteResult.CheckSaveChange -> dialogHolder.showSaveChanges(
            saveNoteAndNavBack = { noteViewModel.saveNoteAndNavBack(titleState.value, textState.value) },
            doNotSaveAndNavBack = noteViewModel::doNotSaveAndNavBack,
        )
        is NoteResult.NavBack -> navBack()
        is NoteResult.Error -> dialogHolder.showError(noteResult.message)
    }
    NoteDetailBody(
        snackbarHostState = snackbarHostState,
        titleState = titleState,
        textState = textState,
        onBackClick = requireNotNull(backWrapper.handler),
        onSaveClick = noteViewModel::saveNote,
        onEditClick = noteViewModel::editTitle,
        onDeleteClick = { dialogHolder.showDelete(onDeleteClick = noteViewModel::deleteNote) },
        showLoading = noteResultState.value == NoteResult.Loading,
    )
}

@Composable
fun NoteDetailBody(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    titleState: MutableState<String> = mutableStateOf("Title"),
    textState: MutableState<String> = mutableStateOf("Text"),
    onBackClick: () -> Unit = {},
    onSaveClick: (title: String?, text: String) -> Unit = { _, _ -> },
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    showLoading: Boolean = true,
) = Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    topBar = {
        TopAppBar(
            title = { Text(text = titleState.value, maxLines = 1) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = Icons.Default.ArrowBack.name
                    )
                }
            },
            actions = {
                IconButton(onClick = { onSaveClick(titleState.value, textState.value) }) {
                    Icon(Icons.Default.Save, contentDescription = stringResource(MR.strings.action_save_note))
                }
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Title, contentDescription = stringResource(MR.strings.action_edit_title))
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = stringResource(MR.strings.action_delete_note))
                }
            }
        )
    }) { paddingValues ->
    Box(modifier = Modifier.padding(paddingValues)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (showLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            TextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                modifier = Modifier.weight(1F).fillMaxWidth().padding(8.dp),
                label = { Text(stringResource(MR.strings.type_text)) },
            )
        }
        themePrefs.showDialogIfNeed()
    }
}

@Preview
@Composable
fun PreviewNoteDetailBody() = PreferableMaterialTheme { NoteDetailBody() }