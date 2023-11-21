package com.thebrownfoxx.petrealm.ui.screens.pets

import com.thebrownfoxx.petrealm.models.Pet

sealed class EditPetDialogState {
    data object Hidden : EditPetDialogState()
    data class Visible(
        val pet: Pet,
        val petName: String = pet.name,
        val petAge: Int? = pet.age,
        val petType: String = pet.type,
        val ownerName: String = pet.owner?.name?:"",
        val hasPetNameWarning: Boolean = false,
        val hasPetAgeWarning: Boolean = false,
        val hasPetTypeWarning: Boolean = false,
        val hasOwnerNameWarning: Boolean = false,
    ) : EditPetDialogState() {
        val hasWarning =
            hasPetNameWarning || hasPetAgeWarning || hasPetTypeWarning
    }

}

class EditPetDialogStateChangeListener(
    val initiateEditPetDialog: (pet: Pet) -> Unit,
    val onHideEditPetDialog: () -> Unit,
    val onPetNameChange: (String) -> Unit,
    val onPetAgeChange: (String) -> Unit,
    val onPetTypeChange: (String) -> Unit,
    val onOwnerNameChange: (String) -> Unit,
    val onEditPet: () -> Unit
)
