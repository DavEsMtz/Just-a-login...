package com.example.justalogin.ui.complosable

import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.justalogin.R


@Composable
fun DrawMessageAlertDialog(dialogContent: DialogContent) {
    with(dialogContent) {
        MessageAlertDialog(
            title = title, description = description, onConfirm = onConfirm
        )
    }
}

@Composable
fun MessageAlertDialog(
    title: String, description: String, onConfirm: () -> Unit
) {
    Column {
        var dialogVisibility by remember { mutableStateOf(true) }
        if (dialogVisibility) {
            AlertDialog(onDismissRequest = { },
                title = {
                    Text(text = title)
                }, text = {
                    Text(description)
                }, confirmButton = {
                    Button(onClick = {
                        dialogVisibility = false
                        onConfirm()
                    }) {
                        Text(stringResource(id = R.string.legend_confirm))
                    }
                })
        }

    }
}

data class DialogContent(
    val title: String,
    val description: String,
    val onConfirm: () -> Unit,
    val onCancel: () -> Unit = {},
    val onDismiss: () -> Unit = {}
)

@Preview(showBackground = false)
@Composable
fun PreviewMessageAlertDialog() {
    MessageAlertDialog(title = "Title", description = "Description", onConfirm = {})
}