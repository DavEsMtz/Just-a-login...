package com.example.justalogin.ui.complosable

import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.justalogin.R


@Preview
@Composable
fun DrawProgressIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(size = 64.dp),
        color = Color(R.color.purpleDark),
        strokeWidth = 6.dp
    )
}