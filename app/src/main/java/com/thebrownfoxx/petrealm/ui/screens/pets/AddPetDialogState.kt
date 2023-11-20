package com.thebrownfoxx.petrealm.ui.screens.pets

sealed class AddPetDialogState {
    data object Hidden : AddPetDialogState()
    data class Visible(
        val petName: String = "",
        val petAge: Int? = null,
        val petType: String = "",
        val hasOwner: Boolean = false,
        val ownerName: String = "",
        val hasPetNameWarning: Boolean = false,
        val hasPetAgeWarning: Boolean = false,
        val hasPetTypeWarning: Boolean = false,
        val hasOwnerNameWarning: Boolean = false,
    ) : AddPetDialogState() {
        val hasWarning =
            hasPetNameWarning || hasPetAgeWarning || hasPetTypeWarning
    }

}

class AddPetDialogStateChangeListener(
    val onShowAddPetDialog: () -> Unit,
    val onHideAddPetDialog: () -> Unit,
    val onPetNameChange: (String) -> Unit,
    val onPetAgeChange: (String) -> Unit,
    val onPetTypeChange: (String) -> Unit,
    val onOwnerNameChange: (String) -> Unit,
    val onAddPet: () -> Unit
)
