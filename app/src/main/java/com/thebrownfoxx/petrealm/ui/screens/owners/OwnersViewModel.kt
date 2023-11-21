package com.thebrownfoxx.petrealm.ui.screens.owners

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamthelegend.enchantmentorder.extensions.combineToStateFlow
import com.hamthelegend.enchantmentorder.extensions.mapToStateFlow
import com.hamthelegend.enchantmentorder.extensions.search
import com.thebrownfoxx.petrealm.models.Owner
import com.thebrownfoxx.petrealm.models.Pet
import com.thebrownfoxx.petrealm.realm.RealmDatabase
import com.thebrownfoxx.petrealm.realm.models.RealmOwner
import com.thebrownfoxx.petrealm.ui.screens.pets.AddPetDialogState
import com.thebrownfoxx.petrealm.ui.screens.pets.EditPetDialogState
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

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun updateSearchQuery(newQuery: String) {
        _searchQuery.update { newQuery }
    }

    val owners = combineToStateFlow(
        database.getAllOwners(),
        searchQuery,
        scope = viewModelScope,
        initialValue = emptyList(),
    ) { realmOwners, searchQuery ->
        realmOwners.map { realmOwner ->
            Owner(
                id = realmOwner.id.toHexString(),
                name = realmOwner.name,
                pets = realmOwner.pets.map {realmPet ->
                    Pet(
                        id = realmPet.id.toHexString(),
                        name = realmPet.name,
                        age = realmPet.age,
                        type = realmPet.type,
                        owner = null,
                    )
                },
            )
        }.search(searchQuery) {it.name}
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

    fun updateOwnerName(newOwnerName: String) {
        _ownerDetailsState.update {
            if (it is OwnerListenerState.Visible) {
                it.copy(
                     ownerName = newOwnerName,
                     hasOwnerNameWarning = false,
                )
            } else it
        }
    }

    fun updateOwnerDetails(){
        var state = ownerListenerState.value
        with(state){
            if(this is OwnerListenerState.Visible){
                if (ownerName.isBlank()) state = copy(hasOwnerNameWarning = true)
                else{
                    viewModelScope.launch {
                        database.updateOwner(
                            owner = owner,
                            newName = ownerName,
                        )
                    }
                }
                _ownerDetailsState.update { OwnerListenerState.Hidden }
            }
        }
    }

    fun disownPet(pet: Pet){
        viewModelScope.launch {
            database.disownPet(pet)
        }
    }
}