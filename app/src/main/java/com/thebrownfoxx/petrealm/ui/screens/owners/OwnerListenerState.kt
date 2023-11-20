package com.thebrownfoxx.petrealm.ui.screens.owners

import com.thebrownfoxx.petrealm.models.Owner
import com.thebrownfoxx.petrealm.models.Pet
import com.thebrownfoxx.petrealm.models.PetType

sealed class OwnerListenerState{

    data object Hidden: OwnerListenerState()
    data class Visible(
        val owner: Owner,
    ): OwnerListenerState()

}

class OwnerStateChangeListener (
    val onDelete: (Owner)-> Unit,
    val updateOwnerName: (String)-> Unit,
    val initiateView: (Owner)-> Unit,
    val onHideViewOwner: ()-> Unit,
)