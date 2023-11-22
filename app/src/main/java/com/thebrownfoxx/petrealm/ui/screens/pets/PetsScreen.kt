package com.thebrownfoxx.petrealm.ui.screens.pets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.Navigation
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.thebrownfoxx.components.extension.plus
import com.thebrownfoxx.petrealm.models.Pet
import com.thebrownfoxx.petrealm.models.Sample
import com.thebrownfoxx.petrealm.ui.screens.Components.EmptyScreen
import com.thebrownfoxx.petrealm.ui.screens.Components.FabItem
import com.thebrownfoxx.petrealm.ui.screens.Components.MultiFloatingActionButton
import com.thebrownfoxx.petrealm.ui.screens.Components.SearchTextField
import com.thebrownfoxx.petrealm.ui.screens.destinations.OwnersDestination
import com.thebrownfoxx.petrealm.ui.screens.destinations.PetsDestination
import com.thebrownfoxx.petrealm.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetsScreen(
    navigator: DestinationsNavigator?,
    petTypes: List<String>,
    pets: List<Pet>,
    searchQuery: String,
    editPetDialogState: EditPetDialogState,
    editPetDialogStateChangeListener: EditPetDialogStateChangeListener,
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
        bottomBar = {
            MultiFloatingActionButton(
                fabIcon = Icons.Default.Add,
                items = arrayListOf(
                    FabItem(
                        icon= Icons.Default.People,
                        label = "Owner",
                        onFabItemClicked = {navigator?.navigate(OwnersDestination)}
                    ),
                    FabItem(
                        icon = Icons.Default.Add,
                        label = "Add Pet",
                        onFabItemClicked = addPetDialogStateChangeListener.onShowAddPetDialog
                    ),
                )
            )
        }
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
                            onAdopt = {addPetOwnerDialogStateChangeListener.initiateAddOwner(pet)},
                            onView = {editPetDialogStateChangeListener.initiateEditPetDialog(pet)}
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
    EditPetDialog(
        petTypes = petTypes,
        state = editPetDialogState,
        stateChangeListener = editPetDialogStateChangeListener
    )
}

@Preview
@Composable
fun PetsScreenPreview() {
    AppTheme {
        PetsScreen(
            navigator = null,
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
            ),
            editPetDialogState = EditPetDialogState.Hidden,
            editPetDialogStateChangeListener = EditPetDialogStateChangeListener(
                onEditPet = {},
                onOwnerNameChange = {},
                initiateEditPetDialog = {},
                onHideEditPetDialog = {},
                onPetAgeChange = {},
                onPetNameChange = {},
                onPetTypeChange = {},
            )
        )
    }
}