package com.thebrownfoxx.petrealm.ui.screens.owners

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun Owners(navigator: DestinationsNavigator) {
    val viewModel: OwnersViewModel = viewModel()

    with(viewModel) {
        val owners by owners.collectAsStateWithLifecycle()
        val ownerListenerState by ownerListenerState.collectAsStateWithLifecycle()
        val searchQuery by searchQuery.collectAsStateWithLifecycle()

        OwnersScreen(
            navigator = navigator,
            owners = owners,
            searchQuery = searchQuery,
            onSearchQueryChange = ::updateSearchQuery,
            ownerListenerState = ownerListenerState,
            ownerStateChangeListener = OwnerStateChangeListener(
                onDelete = ::deleteOwner,
                initiateView = ::initiateViewOwner,
                onHideViewOwner = ::hideViewOwner,
                updateOwnerName = ::updateOwnerName,
                onUpdate = ::updateOwnerDetails,
                onDisown = ::disownPet,
            ),
        )
    }
}