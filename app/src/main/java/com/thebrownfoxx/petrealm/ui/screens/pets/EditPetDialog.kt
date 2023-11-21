package com.thebrownfoxx.petrealm.ui.screens.pets

import PetTypeSpinner
import android.widget.SpinnerAdapter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thebrownfoxx.components.FilledButton
import com.thebrownfoxx.components.TextButton
import com.thebrownfoxx.petrealm.models.PetType
import com.thebrownfoxx.petrealm.realm.RealmDatabase
import com.thebrownfoxx.petrealm.realm.models.RealmPetType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun EditPetDialog(
    petTypes: List<String>,
    state: EditPetDialogState,
    stateChangeListener: EditPetDialogStateChangeListener,
    modifier: Modifier = Modifier,
) {
    if (state is EditPetDialogState.Visible) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = stateChangeListener.onHideEditPetDialog,
            title = { Text(text = "Pet Details") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = "Owned By: ${state.ownerName}")
                    TextField(
                        label = { Text(text = "Name") },
                        value = state.petName,
                        onValueChange = stateChangeListener.onPetNameChange,
                        isError = state.hasPetNameWarning,
                        trailingIcon = {
                            if (state.hasPetNameWarning)
                                Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colorScheme.error)
                        },
                    )

                    TextField(
                        label = { Text(text = "Age") },
                        modifier = Modifier.wrapContentWidth(),
                        value = state.petAge?.toString() ?: "",
                        onValueChange = stateChangeListener.onPetAgeChange,
                        isError = state.hasPetAgeWarning,
                        trailingIcon = {
                            if (state.hasPetAgeWarning)
                                Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colorScheme.error)
                        },
                    )

                    PetTypeSpinner(
                        petTypes = petTypes,
                        isError = state.hasPetTypeWarning,
                        selectedItem = state.petType,
                        onItemSelected = stateChangeListener.onPetTypeChange,
                        placeHolder = "Type",
                    )

                }
            },
            dismissButton = {
                TextButton(
                    text = "Cancel",
                    onClick = stateChangeListener.onHideEditPetDialog,
                )
            },
            confirmButton = {
                FilledButton(
                    text = "Update",
                    onClick = stateChangeListener.onEditPet,

                )}
        )
    }
}