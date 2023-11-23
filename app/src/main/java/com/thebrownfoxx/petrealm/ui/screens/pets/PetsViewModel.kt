package com.thebrownfoxx.petrealm.ui.screens.pets

import androidx.compose.material3.Text
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamthelegend.enchantmentorder.extensions.combineToStateFlow
import com.hamthelegend.enchantmentorder.extensions.mapToStateFlow
import com.hamthelegend.enchantmentorder.extensions.search
import com.thebrownfoxx.petrealm.models.Owner
import com.thebrownfoxx.petrealm.models.Pet
import com.thebrownfoxx.petrealm.realm.RealmDatabase
import com.thebrownfoxx.petrealm.realm.models.RealmPet
import com.thebrownfoxx.petrealm.realm.models.RealmPetType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class PetsViewModel : ViewModel() {
    private val database = RealmDatabase()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()



    val petTypes: StateFlow<List<String>> = database.getAllPetTypes()
        .mapToStateFlow(
            scope = viewModelScope,
            initialValue = emptyList(),
            transform = { list ->
                list.map { it.name }
            }
        )

    val pets = combineToStateFlow(
        database.getAllPets(),
        searchQuery,
        scope = viewModelScope,
        initialValue = emptyList(),
    ) { realmPets, searchQuery ->
        realmPets.map { realmPet ->
            Pet(
                id = realmPet.id.toHexString(),
                name = realmPet.name,
                age = realmPet.age,
                type = realmPet.type,
                owner = realmPet.owner?.let { realmOwner ->
                    Owner(
                        id = realmOwner.id.toHexString(),
                        name = realmOwner.name,
                        pets = emptyList(),
                    )
                },
            )
        }.search(searchQuery) {it.name}
    }

    private val _addPetDialogState = MutableStateFlow<AddPetDialogState>(AddPetDialogState.Hidden)
    val addPetDialogState = _addPetDialogState.asStateFlow()

    private val _addPetOwnerDialogState = MutableStateFlow<AddPetOwnerDialogState>(AddPetOwnerDialogState.Hidden)
    val addPetOwnerDialogState = _addPetOwnerDialogState.asStateFlow()



    private val _editPetDialogState = MutableStateFlow<EditPetDialogState>(EditPetDialogState.Hidden)
    val editPetDialogState = _editPetDialogState.asStateFlow()

    fun hidePetOwnerDialog(){
        _addPetOwnerDialogState.update { AddPetOwnerDialogState.Hidden }
    }

    fun updatePetOwnerName(newOwnerName: String) {
        _addPetOwnerDialogState.update {
            if (it is AddPetOwnerDialogState.Visible) {
                it.copy(
                    ownerName = newOwnerName,
                    hasOwnerNameWarning = false,
                )
            } else it
        }
    }
    fun initiateAddOwner(pet: Pet){
        _addPetOwnerDialogState.update { AddPetOwnerDialogState.Visible("",pet) }
    }

    fun initiateEditPetDialog(pet: Pet){
        _editPetDialogState.update { EditPetDialogState.Visible(pet) }
    }

    fun hideEditPetDialog(){
        _editPetDialogState.update { EditPetDialogState.Hidden }
    }



    fun addPetOwner(){
        var state = addPetOwnerDialogState.value
        with(state){
            if(this is AddPetOwnerDialogState.Visible){
                if(ownerName.isBlank()) state = copy(hasOwnerNameWarning = true)
                else{
                    viewModelScope.launch {
                        database.addOwner(
                            ownerName = ownerName,
                            pet = pet,
                        )
                    }
                    state = AddPetOwnerDialogState.Hidden
                }
                _addPetOwnerDialogState.update { state }
            }
        }
    }

    fun updateSearchQuery(newQuery: String) {
        _searchQuery.update { newQuery }
    }

    fun showPetDialog() {
        _addPetDialogState.update { state ->
            if (state == AddPetDialogState.Hidden) AddPetDialogState.Visible() else state
        }
    }

    fun hidePetDialog() {
        _addPetDialogState.update { AddPetDialogState.Hidden }
    }

    fun editPet(){
        var state = editPetDialogState.value
        with(state){
            if(this is EditPetDialogState.Visible){
                if (petName.isBlank() || petAge == null || petType.isBlank()) {
                    state = copy(
                        hasPetNameWarning = petName.isBlank(),
                        hasPetAgeWarning = petAge == null,
                        hasPetTypeWarning = petType.isBlank()
                    )
                }
                else{
                    viewModelScope.launch {
                        database.editPet(
                            pet = pet,
                            petName = petName,
                            petAge = petAge,
                            petType = petType,
                            ownerName = ownerName
                        )
                    }
                    state = EditPetDialogState.Hidden
                }
                _editPetDialogState.update { state }
            }
        }
    }

    fun updateNewPetName(newPetName: String){
        var state = editPetDialogState.value
        _editPetDialogState.update {
            if(it is EditPetDialogState.Visible){
                it.copy(
                    petName = newPetName,
                    hasPetNameWarning = false,
                )
            } else it
        }
    }

    fun updateNewPetAge(newPetAge: String){
        _editPetDialogState.update {
            if (it is EditPetDialogState.Visible) {
                val petAge = when (newPetAge) {
                    "" -> null
                    else -> newPetAge.toIntOrNull() ?: it.petAge
                }
                it.copy(
                    petAge = petAge,
                    hasPetAgeWarning = false,
                )
            } else it
        }
    }

    fun updateNewPetType(newPetType: String){
        _editPetDialogState.update {
            if(it is EditPetDialogState.Visible){
                it.copy(
                    petType = newPetType,
                    hasPetTypeWarning = false,
                )
            } else it
        }
    }

    fun updateNewPetOwner(newPetOwner: String){
        _editPetDialogState.update {
            if(it is EditPetDialogState.Visible){
                it.copy(
                    ownerName = newPetOwner,
                    hasOwnerNameWarning = false,
                )
            } else it
        }
    }

    fun updatePetName(newPetName: String) {
        _addPetDialogState.update {
            if (it is AddPetDialogState.Visible) {
                it.copy(
                    petName = newPetName,
                    hasPetNameWarning = false,
                )
            } else it
        }
    }

    fun updatePetAge(newPetAge: String) {
        _addPetDialogState.update {
            if (it is AddPetDialogState.Visible) {
                val petAge = when (newPetAge) {
                    "" -> null
                    else -> newPetAge.toIntOrNull() ?: it.petAge
                }
                it.copy(
                    petAge = petAge,
                    hasPetAgeWarning = false,
                )
            } else it
        }
    }

    fun updatePetType(newPetType: String) {
        _addPetDialogState.update {
            if (it is AddPetDialogState.Visible) {
                it.copy(
                    petType = newPetType,
                    hasPetTypeWarning = false,
                )
            } else it
        }
    }


    fun updateOwnerName(newOwnerName: String) {
        _addPetDialogState.update {
            if (it is AddPetDialogState.Visible) {
                it.copy(
                    ownerName = newOwnerName,
                    hasOwnerNameWarning = false,
                )
            } else it
        }
    }

    fun addPet(){
        var state = addPetDialogState.value
        with(state) {
            if (this is AddPetDialogState.Visible) {
                if (petName.isBlank() || petAge == null || petType.isBlank()) {
                    state = copy(
                        hasPetNameWarning = petName.isBlank(),
                        hasPetAgeWarning = petAge == null,
                        hasPetTypeWarning = petType.isBlank()
                    )
                }
                else {
                    viewModelScope.launch {
                        database.addPet(
                            name = petName,
                            age = petAge,
                            type = petType,
                            ownerName = ownerName,
                        )
                    }
                    state = AddPetDialogState.Hidden
                }
                _addPetDialogState.update { state }
            }
        }
    }

    fun deletePet(pet: Pet){
        viewModelScope.launch {
            database.deletePet(id = ObjectId(pet.id))
        }
    }


}