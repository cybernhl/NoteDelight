package com.softartdev.notedelight.ui.dialog.security

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.softartdev.mr.contextLocalized
import com.softartdev.notedelight.MR
import com.softartdev.notedelight.shared.presentation.settings.security.change.ChangeResult
import com.softartdev.notedelight.shared.presentation.settings.security.change.ChangeViewModel
import com.softartdev.notedelight.ui.PasswordField
import com.softartdev.notedelight.ui.dialog.PreviewDialog
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch

@Composable
fun ChangePasswordDialog(dismissDialog: () -> Unit, changeViewModel: ChangeViewModel) {
    val changeResultState: State<ChangeResult> = changeViewModel.resultStateFlow.collectAsState()
    DisposableEffect(changeViewModel) {
        onDispose(changeViewModel::onCleared)
    }
    var oldLabelResource by remember { mutableStateOf(MR.strings.enter_old_password) }
    var oldError by remember { mutableStateOf(false) }
    val oldPasswordState: MutableState<String> = remember { mutableStateOf("") }
    var newLabelResource by remember { mutableStateOf(MR.strings.enter_new_password) }
    var newError by remember { mutableStateOf(false) }
    val newPasswordState: MutableState<String> = remember { mutableStateOf("") }
    var repeatLabelResource by remember { mutableStateOf(MR.strings.repeat_new_password) }
    var repeatError by remember { mutableStateOf(false) }
    val repeatPasswordState: MutableState<String> = remember { mutableStateOf("") }
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    when (val changeResult: ChangeResult = changeResultState.value) {
        is ChangeResult.InitState, is ChangeResult.Loading -> Unit
        is ChangeResult.Success -> dismissDialog()
        is ChangeResult.OldEmptyPasswordError -> {
            oldLabelResource = MR.strings.empty_password
            oldError = true
        }
        is ChangeResult.NewEmptyPasswordError -> {
            newLabelResource = MR.strings.empty_password
            newError = true
        }
        is ChangeResult.PasswordsNoMatchError -> {
            repeatLabelResource = MR.strings.passwords_do_not_match
            repeatError = true
        }
        is ChangeResult.IncorrectPasswordError -> {
            oldLabelResource = MR.strings.incorrect_password
            oldError = true
        }
        is ChangeResult.Error -> coroutineScope.launch {
            snackbarHostState.showSnackbar(changeResult.message ?: MR.strings.error_title.contextLocalized())
        }
    }
    ShowChangePasswordDialog(
        showLoaing = changeResultState.value is ChangeResult.Loading,
        oldLabelResource = oldLabelResource,
        oldError = oldError,
        oldPasswordState = oldPasswordState,
        newLabelResource = newLabelResource,
        newError = newError,
        newPasswordState = newPasswordState,
        repeatLabelResource = repeatLabelResource,
        repeatError = repeatError,
        repeatPasswordState = repeatPasswordState,
        snackbarHostState = snackbarHostState,
        dismissDialog = dismissDialog
    ) { changeViewModel.checkChange(oldPassword = oldPasswordState.value, newPassword = newPasswordState.value, repeatNewPassword = repeatPasswordState.value) }
}

@Composable
fun ShowChangePasswordDialog(
    showLoaing: Boolean = true,
    oldLabelResource: StringResource = MR.strings.enter_old_password,
    oldError: Boolean = false,
    oldPasswordState: MutableState<String> = mutableStateOf("old password"),
    newLabelResource: StringResource = MR.strings.enter_new_password,
    newError: Boolean = false,
    newPasswordState: MutableState<String> = mutableStateOf("new password"),
    repeatLabelResource: StringResource = MR.strings.repeat_new_password,
    repeatError: Boolean = true,
    repeatPasswordState: MutableState<String> = mutableStateOf("repeat new password"),
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    dismissDialog: () -> Unit = {},
    onChangeClick: () -> Unit = {},
) = AlertDialog(
    title = { Text(text = stringResource(MR.strings.dialog_title_change_password)) },
    text = {
        Column {
            if (showLoaing) LinearProgressIndicator()
            PasswordField(
                passwordState = oldPasswordState,
                label = stringResource(oldLabelResource),
                isError = oldError,
                contentDescription = stringResource(MR.strings.enter_old_password),
            )
            PasswordField(
                passwordState = newPasswordState,
                label = stringResource(newLabelResource),
                isError = newError,
                contentDescription = stringResource(MR.strings.enter_new_password),
            )
            PasswordField(
                passwordState = repeatPasswordState,
                label = stringResource(repeatLabelResource),
                isError = repeatError,
                contentDescription = stringResource(MR.strings.repeat_new_password),
            )
            SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    },
    confirmButton = { Button(onClick = onChangeClick) { Text(stringResource(MR.strings.yes)) } },
    dismissButton = { Button(onClick = dismissDialog) { Text(stringResource(MR.strings.cancel)) } },
    onDismissRequest = dismissDialog,
)

@Preview
@Composable
fun PreviewChangePasswordDialog() = PreviewDialog { ShowChangePasswordDialog() }