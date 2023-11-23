package com.thebrownfoxx.petrealm.ui.screens.owners

import com.thebrownfoxx.petrealm.models.Owner
import com.thebrownfoxx.petrealm.models.Pet
import com.thebrownfoxx.petrealm.models.PetType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList

sealed class OwnerListenerState{

    data object Hidden: OwnerListenerState()
    data class Visible(
        val owner: Owner,
        val pet: List<Pet> = owner.pets,
        val ownerName: String = owner.name,
        val hasOwnerNameWarning: Boolean = false,
    ): OwnerListenerState()

}

class OwnerStateChangeListener (
    val onDelete: (Owner)-> Unit,
    val updateOwnerName: (String)-> Unit,
    val initiateView: (Owner)-> Unit,
    val onHideViewOwner: ()-> Unit,
    val onUpdate:()-> Unit,
    val onDisown: (Pet)-> Unit,
)