import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thebrownfoxx.petrealm.models.PetType
import com.thebrownfoxx.petrealm.realm.models.RealmPetType
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetTypeSpinner(
    petTypes: List<String>,
    selectedItem: String,
    isError: Boolean,
    placeHolder: String,
    onItemSelected: (selectedItem: String) -> Unit) {

    var expandedState by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expandedState,
            onExpandedChange = {
                expandedState = !expandedState
            }
        ) {
            TextField(
                label = { Text(text = placeHolder)},
                value = selectedItem,
                onValueChange = onItemSelected,
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedState) },
                modifier = Modifier.menuAnchor(),
                isError = isError,
            )
            ExposedDropdownMenu(
                expanded = expandedState,
                onDismissRequest = { expandedState = false }
            ) {
                petTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(text = type) },
                        onClick = {
                            expandedState = false
                            onItemSelected(type)
                        })
                }
            }
        }
    }
}


