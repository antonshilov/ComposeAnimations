package com.antonshilov.composeanimations

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay

@Composable
fun AirbnbRowPhotos() {
    var isVisible by remember { mutableStateOf(true) }
    Row(
        Modifier
            .padding(16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isVisible = !isVisible
            }) {
        images.forEachIndexed { index, url ->
            ImageCard(
                imageUrl = url,
                index = index,
                isVisible = isVisible,
            )
        }
    }
}

@Composable
fun ImageCard(
    imageUrl: String, index: Int, isVisible: Boolean
) {
    var isVisibleTarget by remember { mutableStateOf(false) }
    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(index * 100L)
            isVisibleTarget = isVisible
        } else {
            isVisibleTarget = isVisible
        }
    }

    val transition = updateTransition(targetState = isVisibleTarget, label = "ImageAnimation")
    val transitionSpec: @Composable (Transition.Segment<Boolean>.() -> FiniteAnimationSpec<Float>) =
        remember {
            {
                when {
                    true isTransitioningTo false -> snap()
                    else -> spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                }
            }
        }
    val scale = transition.animateFloat(
        transitionSpec = transitionSpec,
        label = "Scale"
    ) { isVisible ->
        if (isVisible) 1f else 0f
    }
    val rotation = transition.animateFloat(
        transitionSpec = transitionSpec,
        label = "Rotation"
    ) { isVisible ->
        if (isVisible) rotationDegrees[index] else 0f
    }

    val modifier = Modifier
        .graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
            rotationZ = rotation.value
            translationX = -30f * index
        }
    PhotoSquare(imageUrl, modifier)
}

@Composable
private fun PhotoSquare(
    imageUrl: String,
    modifier: Modifier,
) {
    val shape = remember { RoundedCornerShape(8.dp) }
    Image(
        painter = rememberAsyncImagePainter(imageUrl, placeholder = ColorPainter(Color.Gray)),
        contentDescription = "Tree",
        modifier = modifier
            .size(64.dp)
            .shadow(elevation = 2.dp, shape)
            .border(width = 4.dp, color = Color.White, shape = shape)
            .padding(4.dp),
        contentScale = ContentScale.Crop
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAirbnbAnimation() {
    MaterialTheme {
        Scaffold { paddingValues ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                AirbnbRowPhotos()
            }
        }
    }
}

private val images = listOf(
    "https://images.unsplash.com/photo-1480074568708-e7b720bb3f09?fit=crop&w=500&h=500&q=80",
    "https://images.unsplash.com/photo-1503594384566-461fe158e797?fit=crop&w=500&h=500&q=80",
    "https://images.unsplash.com/photo-1434082033009-b81d41d32e1c?fit=crop&w=500&h=500&q=80",
    "https://images.unsplash.com/photo-1586175554766-77513ac248d0?fit=crop&w=500&h=500&q=80",
    "https://images.unsplash.com/photo-1564357645071-9726b526a8f2?fit=crop&w=500&h=500&q=80"
)

private val rotationDegrees = listOf(10f, -20f, -5f, 5f, -2f)

class ColorPainter(private val color: Color) : Painter() {
    override val intrinsicSize: Size
        get() = Size.Unspecified

    override fun DrawScope.onDraw() {
        drawRect(color = color)
    }
}