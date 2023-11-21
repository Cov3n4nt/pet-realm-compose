package com.thebrownfoxx.petrealm.ui.screens.pets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun Pets(navigator: DestinationsNavigator) {
    val viewModel: PetsViewModel = viewModel()

    with(viewModel) {
        val petTypes by petTypes.collectAsStateWithLifecycle(emptyList())
        val pets by pets.collectAsStateWithLifecycle()
        val searchQuery by searchQuery.collectAsStateWithLifecycle()
        val addPetDialogState by addPetDialogState.collectAsStateWithLifecycle()
        val addPetOwnerDialogState by addPetOwnerDialogState.collectAsStateWithLifecycle()
        val editPetDialogState by editPetDialogState.collectAsStateWithLifecycle()

        PetsScreen(
            pets = pets,
            petTypes = petTypes,
            searchQuery = searchQuery,
            onSearchQueryChange = ::updateSearchQuery,
            addPetDialogState = addPetDialogState,
            addPetDialogStateChangeListener = AddPetDialogStateChangeListener(
                onShowAddPetDialog = ::showPetDialog,
                onHideAddPetDialog = ::hidePetDialog,
                onPetNameChange = ::updatePetName,
                onPetAgeChange = ::updatePetAge,
                onPetTypeChange = ::updatePetType,
                onOwnerNameChange = ::updateOwnerName,
                onAddPet = ::addPet
            ),

            removePetDialogStateChangeListener = RemovePetDialogStateChangeListener(
                onDeletePet = ::deletePet,
            ),
            addPetOwnerDialogState = addPetOwnerDialogState,
            addPetOwnerDialogStateChangeListener = AddPetOwnerDialogStateChangeListener(
                onOwnerNameChange = ::updatePetOwnerName,
                initiateAddOwner = ::initiateAddOwner,
                onHideAddPetOwnerDialog = ::hidePetOwnerDialog,
                onAddPetOwner = ::addPetOwner,
            ),
            editPetDialogState = editPetDialogState,
            editPetDialogStateChangeListener = EditPetDialogStateChangeListener(
                initiateEditPetDialog = ::initiateEditPetDialog,
                onHideEditPetDialog = ::hideEditPetDialog,
                onPetTypeChange = ::updateNewPetType,
                onPetNameChange = ::updateNewPetName,
                onPetAgeChange = ::updateNewPetAge,
                onOwnerNameChange = ::updateNewPetOwner,
                onEditPet = ::editPet
            )
        )
    }
}