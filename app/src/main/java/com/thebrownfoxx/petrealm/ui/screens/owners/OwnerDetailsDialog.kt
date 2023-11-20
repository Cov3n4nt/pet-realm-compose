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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
                       value = state.owner.name,
                       onValueChange = stateChangeListener.updateOwnerName,
                   )
                   Text(text = "Pet/s Owned: ")
                   Box(modifier = Modifier.fillMaxWidth()){
                       LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                           items(
                               items = state.owner.pets,
                               key = {ownedPet -> ownedPet.id}
                           ) { ownedPet ->
                               Card(modifier = Modifier.fillMaxWidth()) {
                                   Row {
                                       Box(modifier = Modifier
                                           .padding(8.dp)
                                           .align(Alignment.CenterVertically)){
                                           Image(
                                               painter = painterResource(id = if (ownedPet.type == "Cat") R.drawable.cat else R.drawable.dog),
                                               contentDescription = ownedPet.type,
                                               Modifier.size(64.dp),
                                           )
                                       }
                                       Column {
                                           Text(text = "Name: ${ownedPet.name}")
                                           Text(text = "Age: ${ownedPet.age}")
                                           Text(text = "Type: ${ownedPet.type}")
                                       }
                                   }
                               }
                           }
                       }
                   }
               }

           },
           confirmButton = { /*TODO*/ })
    }
}