package com.thebrownfoxx.petrealm.ui.screens.owners

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.thebrownfoxx.petrealm.models.Owner
import com.thebrownfoxx.petrealm.ui.screens.Components.DismissBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerCard(
    owner: Owner,
    onRemove: () -> Unit,
    initiateView: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dismissState = rememberDismissState(
        confirmValueChange = {direction->
            if(direction == DismissValue.DismissedToEnd){
                onRemove()
            }
            true
        }
    )
    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd),
        background = { DismissBackground(dismissState = dismissState) },
        dismissContent = {
            Card(
                modifier = modifier,
                onClick = initiateView,
                ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                ) {
                    Text(text = "Name: ${owner.name}")
                    Text(
                        text = "Pets: ${owner.pets.joinToString(", ") { it.name }}",
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        })
}