package com.thebrownfoxx.petrealm.ui.screens.Components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun MultiFloatingActionButton(
    fabIcon: ImageVector,
    items: List<FabItem>,
    showLabels: Boolean = true,
    onStateChanged: ((state: MultiFabState) -> Unit)? = null
) {
    var currentState by remember { mutableStateOf(MultiFabState.COLLAPSED) }
    val stateTransition: Transition<MultiFabState> =
        updateTransition(targetState = currentState, label = "")
    val stateChange: () -> Unit = {
        currentState = if (stateTransition.currentState == MultiFabState.EXPANDED) {
            MultiFabState.COLLAPSED
        } else MultiFabState.EXPANDED
        onStateChanged?.invoke(currentState)
    }
    val rotation: Float by stateTransition.animateFloat(
        transitionSpec = {
            if (targetState == MultiFabState.EXPANDED) {
                spring(stiffness = Spring.StiffnessLow)
            } else {
                spring(stiffness = Spring.StiffnessMedium)
            }
        },
        label = ""
    ) { state ->
        if (state == MultiFabState.EXPANDED) 45f else 0f
    }
    val isEnable = currentState == MultiFabState.EXPANDED

    BackHandler(isEnable) {
        currentState = MultiFabState.COLLAPSED
    }

    val modifier = if(currentState ==   MultiFabState.EXPANDED)
        Modifier
            .fillMaxSize()
            .clickable(indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                currentState = MultiFabState.COLLAPSED
            } else Modifier.fillMaxSize()

    Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            if (currentState == MultiFabState.EXPANDED) {
                Canvas(modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = 2.2f
                        scaleY = 2.1f
                    }) {
                    translate(150f, top = 300f) {
                        scale(5f) {}
                        drawCircle(Color.Black.copy(alpha = 0.3f), radius = 200.dp.toPx())

                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom,
            ) {
                items.forEach { item ->
                    SmallFloatingActionButtonRow(
                        item = item,
                        stateTransition = stateTransition,
                        showLabel = showLabels
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
                FloatingActionButton(
                    modifier = Modifier.padding(8.dp),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    onClick = {
                        stateChange()
                    }) {
                    
                    Icon(imageVector = fabIcon, contentDescription = null, modifier = Modifier.rotate(rotation))
                }
            }

        }
    }

}
@Composable
fun SmallFloatingActionButtonRow(
    item: FabItem,
    showLabel: Boolean,
    stateTransition: Transition<MultiFabState>
) {
    val alpha: Float by stateTransition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 50)
        }, label = ""
    ) { state ->
        if (state == MultiFabState.EXPANDED) 1f else 0f
    }
    val scale: Float by stateTransition.animateFloat(
        label = ""
    ) { state ->
        if (state == MultiFabState.EXPANDED) 1.0f else 0f
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .alpha(animateFloatAsState((alpha), label = "").value)
            .scale(animateFloatAsState(targetValue = scale, label = "").value)
    ) {
        if (showLabel) {
            Text(
                text = item.label,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(onClick = { item.onFabItemClicked() })
            )
        }
        SmallFloatingActionButton(
            shape = CircleShape,
            modifier = Modifier.padding(4.dp),
            onClick = { item.onFabItemClicked() },
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ) {
            Icon(imageVector = item.icon, contentDescription = item.label)

        }
    }
}

@Preview
@Composable
fun ViewMultiFloatingButton(){
    MultiFloatingActionButton(
        fabIcon = Icons.Default.Add,
        items = arrayListOf(
            FabItem(icon =  Icons.Default.Person, label = "Person",{}),
            FabItem(icon =  Icons.Default.Pets, label = "Person",{})
        ),
    )
}
