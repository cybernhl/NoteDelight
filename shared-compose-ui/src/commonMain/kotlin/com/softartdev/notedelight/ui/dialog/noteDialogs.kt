@file:OptIn(ExperimentalMaterial3Api::class)

package com.softartdev.notedelight.ui.dialog

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.softartdev.notedelight.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun SaveDialog(
    saveNoteAndNavBack: () -> Unit,
    doNotSaveAndNavBack: () -> Unit,
    onDismiss: () -> Unit
) = AlertDialog(
    onDismissRequest = onDismiss,
) {
    AlertDialogContent(
        buttons = {
            Row(Modifier.padding(horizontal = 8.dp)) {
                Button(onClick = onDismiss) { Text(stringResource(MR.strings.cancel)) }
                Spacer(Modifier.width(8.dp))
                Button(onClick = doNotSaveAndNavBack) { Text(stringResource(MR.strings.no)) }
                Spacer(Modifier.width(8.dp))
                Button(onClick = saveNoteAndNavBack) { Text(stringResource(MR.strings.yes)) }
            }
        },
        title = { Text(stringResource(MR.strings.note_changes_not_saved_dialog_title)) },
        text = { Text(stringResource(MR.strings.note_save_change_dialog_message)) },
    )
}

@Composable
fun DeleteDialog(onDeleteClick: () -> Unit, onDismiss: () -> Unit) = ShowDialog(
    title = stringResource(MR.strings.action_delete_note),
    text = stringResource(MR.strings.note_delete_dialog_message),
    onConfirm = onDeleteClick,
    onDismiss = onDismiss
)

@Preview
@Composable
fun PreviewSaveDialog() = PreviewDialog { SaveDialog({}, {}, {}) }

@Preview
@Composable
fun PreviewDeleteDialog() = PreviewDialog { DeleteDialog({}, {}) }