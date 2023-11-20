package com.thebrownfoxx.petrealm.ui.screens.Components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun EmptyScreen(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier,){
        Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
            Icon(
                imageVector = icon,
                contentDescription = "No pets found",
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = text,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}