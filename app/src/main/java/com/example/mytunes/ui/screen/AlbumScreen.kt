package com.example.mytunes.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.session.MediaController
import com.example.mytunes.model.AlbumData
import com.example.mytunes.ui.elements.CoverImage
import com.example.mytunes.ui.elements.SongBanner
import com.example.mytunes.ui.elements.removeParenthesesContent
import com.example.mytunes.ui.viewModel.AlbumUiState
import com.google.common.util.concurrent.ListenableFuture
@Composable
fun AlbumScreen(
    modifier: Modifier = Modifier,
    id: String? = null,
    loadAlbum:(String) -> Unit,
    response: AlbumUiState,
    controllerFuture: ListenableFuture<MediaController>
) {

    LaunchedEffect(true) {
        if (id != null) {
            loadAlbum(id)
        }
    }
    Box (modifier = modifier) {
        when(response) {
            is AlbumUiState.Success -> AlbumContent(
                response = response.albumList.data,
                controllerFuture = controllerFuture
            )
            else -> {}
        }
    }
}

@SuppressLint("RestrictedApi", "RememberReturnType")
@Composable
fun AlbumContent(
    modifier: Modifier = Modifier,
    response: AlbumData,
    controllerFuture: ListenableFuture<MediaController>
) {

    val colorStops = arrayOf(
        0.0f to Color.Transparent,
        0.2f to MaterialTheme.colorScheme.background.copy(alpha = 0.3f),
        0.5f to MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
        0.7f to MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
        0.8f to MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
        1.0f to MaterialTheme.colorScheme.background
    )
    var textWidth by remember { mutableIntStateOf(0) }
    var boxWidth by remember { mutableIntStateOf(0) }
    val infiniteTransition = rememberInfiniteTransition(label = "b")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (textWidth > boxWidth) -textWidth.toFloat() else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )
    response.image[2].url?.let {
        CoverImage(
            photo = it,
            modifier = modifier
                .blur(
                    radiusX = 5.dp,
                    radiusY = 5.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                ).alpha(0.5f)
                .fillMaxWidth()
        )
    }
    Column {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            contentAlignment = Alignment.Center
        ) {
            response.image[2].url?.let {
                CoverImage(
                    photo = it,
                    modifier = modifier.size(100.dp)
                )
            }
        }
        val interactionSource = remember {MutableInteractionSource()}
        Box(
            modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Brush.verticalGradient(colorStops = colorStops))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates ->
                        boxWidth = coordinates.size.width
                    }
                    .clipToBounds(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = removeParenthesesContent(response.name.replace("&amp;", "and").replace("&quot;", "'")),
                    modifier = Modifier
                        .offset(x = offsetX.dp)
                        .padding(start = 0.dp, top = 4.dp)
                        .onGloballyPositioned {
                            textWidth = it.size.width
                        },
                    fontWeight = FontWeight.W400,
                    fontSize = 24.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Visible
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${response.songCount} songs",
                        fontWeight = FontWeight.W400,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Top)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Filled.MusicNote,
                        contentDescription = null,
                        modifier = Modifier.size(10.dp)
                    )
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .indication(interactionSource, indication = null)
                        .size(66.dp)
                        .padding()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Circle,
                        contentDescription = null,
                        modifier = Modifier.size(460.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = Icons.Rounded.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }

        }
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(response.songs) { song ->
                    val photoIndex = song.image.size - 1
                    val songUrlIndex = song.downloadUrl.size - 1
                    if (photoIndex >= 0 && songUrlIndex >= 0) {
                        SongBanner(
                            photo = song.image[photoIndex].url,
                            songName = song.name,
                            artists = song.artists.primary,
                            controllerFuture = controllerFuture,
                            onSongClick = { /*TODO*/ },
                            songUrl = song.downloadUrl[songUrlIndex],
                            modifier = modifier.fillMaxWidth()
                        )
                    }

                }
            }
        }
    }
}


