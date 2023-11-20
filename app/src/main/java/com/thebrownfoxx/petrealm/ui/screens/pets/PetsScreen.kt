package com.thebrownfoxx.petrealm.ui.screens.pets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thebrownfoxx.components.extension.plus
import com.thebrownfoxx.petrealm.models.Pet
import com.thebrownfoxx.petrealm.models.Sample
import com.thebrownfoxx.petrealm.ui.screens.Components.EmptyScreen
import com.thebrownfoxx.petrealm.ui.screens.Components.SearchTextField
import com.thebrownfoxx.petrealm.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetsScreen(
    petTypes: List<String>,
    pets: List<Pet>,
    searchQuery: String,
    addPetOwnerDialogState: AddPetOwnerDialogState,
    addPetOwnerDialogStateChangeListener: AddPetOwnerDialogStateChangeListener,
    onSearchQueryChange: (String) -> Unit,
    addPetDialogState: AddPetDialogState,
    addPetDialogStateChangeListener: AddPetDialogStateChangeListener,
    removePetDialogStateChangeListener: RemovePetDialogStateChangeListener,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
                 SearchTextField(
                     search = searchQuery,
                     onValueChange = onSearchQueryChange,
                     modifier = Modifier
                         .fillMaxWidth()
                         .padding(8.dp),
                 )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = addPetDialogStateChangeListener.onShowAddPetDialog) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ){
            if(pets.isEmpty()){
                EmptyScreen(
                    text = "No pets found",
                    modifier = Modifier
                        .align(Alignment.Center),
                    icon = Icons.Default.Pets,
                    )
            }
            else{
                LazyColumn(
                    contentPadding = contentPadding + PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(
                        items = pets,
                        key = {pet -> pet.id}
                    ) { pet ->
                        PetCard(
                            pet = pet,
                            onRemove = { removePetDialogStateChangeListener.onDeletePet(pet) },
                            onAdopt = {addPetOwnerDialogStateChangeListener.initiateAddOwner(pet)}
                        )
                    }
                }
            }
        }
    }
    AddPetOwnerDialog(
        state = addPetOwnerDialogState,
        stateChangeListener = addPetOwnerDialogStateChangeListener,
    )

    AddPetDialog(
        state = addPetDialogState,
        stateChangeListener = addPetDialogStateChangeListener,
        petTypes = petTypes,
    )
}

@Preview
@Composable
fun PetsScreenPreview() {
    AppTheme {
        PetsScreen(
            pets = Sample.Pets,
            petTypes = Sample.listType,
            searchQuery = "",
            onSearchQueryChange = {},
            addPetDialogState = AddPetDialogState.Hidden,
            addPetDialogStateChangeListener = AddPetDialogStateChangeListener(
                onShowAddPetDialog = {},
                onHideAddPetDialog = {},
                onPetNameChange = {},
                onPetAgeChange = {},
                onPetTypeChange = {},
                onOwnerNameChange = {},
                onAddPet = {},
            ),
            removePetDialogStateChangeListener = RemovePetDialogStateChangeListener(
                onDeletePet = {},
            ),
            addPetOwnerDialogState = AddPetOwnerDialogState.Hidden,
            addPetOwnerDialogStateChangeListener = AddPetOwnerDialogStateChangeListener(
                onHideAddPetOwnerDialog = {},
                onOwnerNameChange = {},
                initiateAddOwner = {},
                onAddPetOwner = {},

            )
        )
    }
}