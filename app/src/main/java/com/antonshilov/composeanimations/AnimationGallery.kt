package com.antonshilov.composeanimations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.ui.unit.dp

data class AnimationItem(val title: String, val content: @Composable () -> Unit)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimationGallery(
    modifier: Modifier = Modifier,
    onItemSelected: (AnimationItem) -> Unit = {}
) {
    val listState = rememberLazyListState()
    val fling = rememberSnapFlingBehavior(listState)
    val animations = listOf(
        AnimationItem("Certificates stack") { CertificatesStack() },
        AnimationItem("Airbnb row photos") { AirbnbRowPhotos() },
        AnimationItem("Airbnb staggered photos") { AirbnbStaggeredPhotos() },
        AnimationItem("Swipe button") { ConfirmationButton() },
        AnimationItem("Spring playground") { SpringPlayground() },
    )
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        flingBehavior = fling,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(animations) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemSelected(item) },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = item.title, style = MaterialTheme.typography.titleLarge)
                    Box(Modifier.padding(top = 16.dp)) {
                        item.content()
                    }
                }
            }
        }
    }
}
