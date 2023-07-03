package com.softartdev.notedelight.ui

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.softartdev.themepref.AlertDialog

@Composable
actual fun PermissionDialog(dismissCallback: () -> Unit) = AlertDialog(
    onDismissRequest = dismissCallback,
    title = { Text("Permissions") },
    text = { Text("Permission not required on JVM platform") },
    buttons = {
        Button(
            onClick = dismissCallback,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
        ) {
            Text("OK")
        }
    }
)