package com.thebrownfoxx.petrealm.ui.screens.pets

import com.thebrownfoxx.petrealm.models.Pet
import com.thebrownfoxx.petrealm.realm.models.RealmPet
import io.realm.kotlin.types.RealmList
import org.mongodb.kbson.ObjectId

sealed class AddPetOwnerDialogState {
    data object Hidden : AddPetOwnerDialogState()

   data class Visible(
       val ownerName: String = "",
       val pet: Pet,
       val hasOwnerNameWarning: Boolean = false,
   ) : AddPetOwnerDialogState()
}
class AddPetOwnerDialogStateChangeListener(
    val onHideAddPetOwnerDialog: () -> Unit,
    val onOwnerNameChange: (String) -> Unit,
    val initiateAddOwner: (Pet) -> Unit,
    val onAddPetOwner: () -> Unit,
)
