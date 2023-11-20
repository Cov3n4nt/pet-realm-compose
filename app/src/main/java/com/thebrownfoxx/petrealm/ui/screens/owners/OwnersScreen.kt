package com.thebrownfoxx.petrealm.ui.screens.owners

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thebrownfoxx.components.extension.plus
import com.thebrownfoxx.petrealm.models.Owner
import com.thebrownfoxx.petrealm.ui.screens.Components.EmptyScreen

@Composable
fun OwnersScreen(
    owners: List<Owner>,
    modifier: Modifier = Modifier,
    ownerStateChangeListener: OwnerStateChangeListener,
    ownerListenerState: OwnerListenerState,

) {
    Scaffold(modifier = modifier) { contentPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize(),
        ){
            if(owners.isEmpty()){
                EmptyScreen(
                    text = "There are no owners found",
                    modifier = Modifier
                        .align(Alignment.Center),
                    icon = Icons.Default.Person,
                )
            }
            else{
                LazyColumn(
                    contentPadding = contentPadding + PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(
                        items = owners,
                        key = { owner -> owner.id}
                    ) { owner ->
                        OwnerCard(
                            owner = owner,
                            onRemove = {ownerStateChangeListener.onDelete(owner)},
                            initiateView = {ownerStateChangeListener.initiateView(owner)},
                        )
                    }
                }
            }
        }
    }
    OwnerDetailsDialog(
        state = ownerListenerState,
        stateChangeListener = ownerStateChangeListener
    )
}

