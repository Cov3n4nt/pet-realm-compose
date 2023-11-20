package com.thebrownfoxx.petrealm.ui.screens.owners

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamthelegend.enchantmentorder.extensions.mapToStateFlow
import com.thebrownfoxx.petrealm.models.Owner
import com.thebrownfoxx.petrealm.models.Pet
import com.thebrownfoxx.petrealm.realm.RealmDatabase
import io.realm.kotlin.ext.asFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class OwnersViewModel: ViewModel() {
    private val database = RealmDatabase()
    val owners = database.getAllOwners().mapToStateFlow(
        scope = viewModelScope,
        initialValue = emptyList(),
    ) { realmOwners ->
        realmOwners.map { realmOwner ->
            Owner(
                id = realmOwner.id.toHexString(),
                name = realmOwner.name,
                pets = realmOwner.pets.map { realmPet ->
                    Pet(
                        id = realmPet.id.toHexString(),
                        name = realmPet.name,
                        age = realmPet.age,
                        type = realmPet.type,
                    )
                }
            )
        }
    }


    private val _ownerDetailsState =
        MutableStateFlow<OwnerListenerState>(OwnerListenerState.Hidden)
    val ownerListenerState = _ownerDetailsState.asStateFlow()

    fun deleteOwner(owner:Owner){
        viewModelScope.launch {
            database.deletOwner(id = ObjectId(owner.id))
        }
    }

    fun hideViewOwner(){
        _ownerDetailsState.update { OwnerListenerState.Hidden }
    }

    fun initiateViewOwner(owner: Owner){
        _ownerDetailsState.update { OwnerListenerState.Visible(owner) }
    }
}