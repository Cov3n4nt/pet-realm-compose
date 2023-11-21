package com.thebrownfoxx.petrealm.ui.screens.owners

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thebrownfoxx.petrealm.models.Owner
import com.thebrownfoxx.petrealm.models.Sample
import com.thebrownfoxx.petrealm.ui.screens.Components.DismissBackground
import com.thebrownfoxx.petrealm.ui.theme.AppTheme

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
                Surface(color = MaterialTheme.colorScheme.primaryContainer) {
                    Row(modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                        Arrangement.Start) {
                        Box(modifier = Modifier
                            .padding(4.dp)
                            .align(Alignment.CenterVertically)
                        ) {
                            Icon(imageVector = Icons.Default.Person, contentDescription = "person icon")
                        }

                        Column(
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth(),
                        ) {
                            Text(text = "Name: ${owner.name}")
                            Text(
                                text = "Pets: ${owner.pets.joinToString(", ") { it.name }}",
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                            )
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun OwnerCardPreview() {
    AppTheme {
        OwnerCard(owner = Sample.owner,
            onRemove = {},
            initiateView = {}
        )
    }
}