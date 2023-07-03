package com.softartdev.notedelight.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.softartdev.notedelight.shared.presentation.files.FilesResult
import com.softartdev.notedelight.shared.presentation.files.FilesViewModel
import com.softartdev.notedelight.ui.dialog.showPermDialog
import com.softartdev.themepref.DialogHolder
import com.softartdev.themepref.LocalThemePrefs

@Composable
fun FileListScreen(
    onBackClick: () -> Unit = {},
    filesViewModel: FilesViewModel
) {
    val fileListState: State<FilesResult> = filesViewModel.resultStateFlow.collectAsState()
    val dialogHolder: DialogHolder = LocalThemePrefs.current.dialogHolder
    DisposableEffect(filesViewModel) {
        filesViewModel.updateFiles()
        onDispose(filesViewModel::onCleared)
    }
    Box {
        FileListScreen(
            fileListState = fileListState,
            onBackClick = onBackClick,
            onItemClicked = filesViewModel::onItemClicked,
            onPermClicked = dialogHolder::showPermDialog
        )
        dialogHolder.showDialogIfNeed()
    }
}

@Composable
expect fun PermissionDialog(dismissCallback: () -> Unit)

@Composable
fun FileListScreen(
    fileListState: State<FilesResult>,
    onBackClick: () -> Unit = {},
    onItemClicked: (text: String) -> Unit = {},
    onPermClicked: () -> Unit = {},
) = Scaffold(
    topBar = {
        TopAppBar(
            title = { Text("File list") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = Icons.Default.ArrowBack.name
                    )
                }
            },
            actions = {
                IconButton(onClick = onPermClicked) {
                    Icon(Icons.Default.PermMedia, contentDescription = "Permissions")
                }
            }
        )
    },
    content = {
        when (val result = fileListState.value) {
            is FilesResult.Loading -> Loader()
            is FilesResult.Success -> {
                val fileList: List<String> = result.result
                if (fileList.isEmpty()) Empty() else FileList(fileList, onItemClicked)
            }

            is FilesResult.Error -> Error(err = result.error ?: "Unknown error")
        }
    }
)

@Composable
fun FileList(fileList: List<String>, onItemClicked: (text: String) -> Unit) {
    val listState = rememberLazyListState()

    LazyColumn(state = listState) {
        items(items = fileList) {
            FileItem(
                fileName = it,
                onItemClicked = onItemClicked,
            )
            Divider()
        }
    }
    LaunchedEffect(key1 = fileList.size, key2 = listState) {
        listState.animateScrollToItem(0)
    }
}

@Composable
fun FileItem(fileName: String, onItemClicked: (fileName: String) -> Unit) = Column(
    modifier = Modifier
        .clickable { onItemClicked(fileName) }
        .fillMaxWidth()
        .padding(4.dp)
        .clearAndSetSemantics { contentDescription = fileName }
) {
    Text(
        text = fileName,
        style = MaterialTheme.typography.h6,
    )
}
