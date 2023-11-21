package com.thebrownfoxx.petrealm.ui.screens.pets

import PetTypeSpinner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thebrownfoxx.components.FilledButton
import com.thebrownfoxx.components.TextButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetOwnerDialog(
    state: AddPetOwnerDialogState,
    stateChangeListener: AddPetOwnerDialogStateChangeListener,
    modifier: Modifier = Modifier,
) {
    if(state is AddPetOwnerDialogState.Visible){
        AlertDialog(
            onDismissRequest =  stateChangeListener.onHideAddPetOwnerDialog ,
            title = { Text(text = "Add Owner")},
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        TextField(
                            label = { Text(text = "Name") },
                            value = state.ownerName,
                            onValueChange = stateChangeListener.onOwnerNameChange,
                            isError = state.hasOwnerNameWarning,
                            trailingIcon = {
                                if (state.hasOwnerNameWarning)
                                    Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colorScheme.error)
                            }
                        )
                }
            },
            confirmButton = { FilledButton(
                text = "Adopt",
                onClick =  stateChangeListener.onAddPetOwner
            ) },
            dismissButton = {TextButton(
                text = "Cancel",
                onClick =  stateChangeListener.onHideAddPetOwnerDialog
            ) }
        )
    }
}