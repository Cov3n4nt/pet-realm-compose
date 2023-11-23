package com.thebrownfoxx.petrealm.ui.screens.owners

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thebrownfoxx.components.FilledButton
import com.thebrownfoxx.petrealm.R
import com.thebrownfoxx.petrealm.models.Pet
import com.thebrownfoxx.petrealm.models.Sample
import com.thebrownfoxx.petrealm.ui.screens.Components.DismissBackground
import com.thebrownfoxx.petrealm.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnedPetCard(
    pet: Pet,
    onDisown: (Pet)->Unit
    ) {
    val dismissState = rememberDismissState(
        confirmValueChange = {direction->
            if(direction == DismissValue.DismissedToEnd){
               onDisown(pet)
            }
            true
        }
    )
    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd),
        background = { DismissBackground(dismissState = dismissState) },
        dismissContent = {
            Card(modifier = Modifier.fillMaxWidth()){
                Row {
                    Box(modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterVertically)){
                        Image(
                            painter = painterResource(id = if (pet.type == "Cat") R.drawable.cat else R.drawable.dog),
                            contentDescription = pet.type,
                            Modifier.size(64.dp),
                        )
                    }
                    Column {
                        Text(text = "Name: ${pet.name}")
                        Text(text = "Age: ${pet.age}")
                        Text(text = "Type: ${pet.type}")
                    }
                }

            }
        }
    )
}

@Preview
@Composable
fun OwnedPetCardPreview() {
    AppTheme {
        OwnedPetCard(pet = Sample.Pet, onDisown = {})
    }
}