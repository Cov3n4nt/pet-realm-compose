package com.thebrownfoxx.petrealm.ui.screens.pets

import com.thebrownfoxx.petrealm.models.Pet
import org.mongodb.kbson.ObjectId

class RemovePetDialogStateChangeListener(
    val onDeletePet: (Pet) -> Unit,
)