package com.thebrownfoxx.petrealm.ui.screens.Components

import androidx.compose.ui.graphics.vector.ImageVector


enum class MultiFabState {
    COLLAPSED, EXPANDED
}

class FabItem(
    val icon: ImageVector,
    val label: String,
    val onFabItemClicked: () -> Unit
)