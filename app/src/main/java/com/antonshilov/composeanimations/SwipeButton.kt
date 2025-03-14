import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.antonshilov.composeanimations.ui.theme.ComposeAnimationsTheme
import kotlin.math.roundToInt

val GreenColor = Color(0xFF2FD286)

enum class ConfirmationState {
    Default, Confirmed
}

@Composable
fun ConfirmationButton(modifier: Modifier = Modifier) {

    val width = 350.dp
    val dragSize = 50.dp
    val coroutineScope = rememberCoroutineScope()

    val density = LocalDensity.current
    val sizePx = with(density) { (width - dragSize).toPx() }

    val draggableState = remember {
        AnchoredDraggableState(
            initialValue = ConfirmationState.Default,
            anchors = DraggableAnchors {
                ConfirmationState.Default at 0f
                ConfirmationState.Confirmed at sizePx
            },

            )
    }

    val progress = draggableState.progress(ConfirmationState.Default, ConfirmationState.Confirmed)

    Box(
        modifier = modifier
            .width(width)
            .anchoredDraggable(
                state = draggableState,
                orientation = Orientation.Horizontal,
            )
            .background(GreenColor, RoundedCornerShape(dragSize))
    ) {
        Column(
            Modifier
                .align(Alignment.Center)
                .alpha(1f - progress),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Order 1000 rub", color = Color.White, fontSize = 18.sp)
            Text("Swipe to confirm", color = Color.White, fontSize = 12.sp)
        }

        DraggableControl(
            modifier = Modifier
                .offset { IntOffset(draggableState.offset.roundToInt(), 0) }
                .size(dragSize),
            progress = progress
        )
    }

}

@Composable
private fun DraggableControl(
    modifier: Modifier,
    progress: Float
) {
    Box(
        modifier
            .padding(4.dp)
            .shadow(elevation = 2.dp, CircleShape, clip = false)
            .background(Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        val isConfirmed = progress >= 0.8f
        Crossfade(targetState = isConfirmed) {
            if (it) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                    tint = GreenColor
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = null,
                    tint = GreenColor
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmationButtonPreview() {
    ComposeAnimationsTheme {
        ConfirmationButton()
    }
}
