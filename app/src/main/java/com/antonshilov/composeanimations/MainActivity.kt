package com.antonshilov.composeanimations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import com.antonshilov.composeanimations.ui.theme.ComposeAnimationsTheme
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAnimationsTheme {
                Surface {
                    AppContent()
                }
            }
        }
    }
}

private sealed interface Screen {
    data object Gallery : Screen
    data class Detail(val item: AnimationItem) : Screen
}

@Composable
private fun AppContent() {
    var screen: Screen by remember { mutableStateOf<Screen>(Screen.Gallery) }
    when (val current = screen) {
        Screen.Gallery -> AnimationGallery { screen = Screen.Detail(it) }
        is Screen.Detail -> AnimationDetail(item = current.item, onBack = { screen = Screen.Gallery })
    }
}

@Composable
private fun AnimationDetail(item: AnimationItem, onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    Box(Modifier.fillMaxSize()) {
        IconButton(onClick = onBack, modifier = Modifier.align(Alignment.TopStart)) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            item.content()
        }
    }
}
