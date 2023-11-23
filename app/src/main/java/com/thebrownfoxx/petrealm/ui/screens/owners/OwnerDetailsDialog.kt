package com.thebrownfoxx.petrealm.ui.screens.owners

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thebrownfoxx.components.FilledButton
import com.thebrownfoxx.petrealm.R

@Composable
fun OwnerDetailsDialog(
    state: OwnerListenerState,
    stateChangeListener: OwnerStateChangeListener,
) {
    if(state is OwnerListenerState.Visible){
       AlertDialog(
           onDismissRequest = stateChangeListener.onHideViewOwner,
           title = { Text(text = "Owner Details")},
           text = {
               Column(verticalArrangement = Arrangement.spacedBy(16.dp)){
                   TextField(
                       label = { Text(text = "Name")},
                       value = state.ownerName,
                       onValueChange = stateChangeListener.updateOwnerName,
                       isError = state.hasOwnerNameWarning,
                       trailingIcon = {
                           if (state.hasOwnerNameWarning)
                               Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colorScheme.error)
                       },

                   )
                   Text(text = "Pet/s Owned: ")
                   Box(modifier = Modifier.fillMaxWidth()){
                       LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                           items(
                               items = state.pet,
                               key = {ownedPet -> ownedPet.id}
                           ) { ownedPet ->
                              OwnedPetCard(
                                  pet = ownedPet,
                                  onDisown = {stateChangeListener.onDisown(ownedPet)}
                              )
                           }
                       }
                   }
               }

           },
           confirmButton = {
               FilledButton(
               text = "Update",
               onClick = stateChangeListener.onUpdate
               )
           }
       )
    }
}