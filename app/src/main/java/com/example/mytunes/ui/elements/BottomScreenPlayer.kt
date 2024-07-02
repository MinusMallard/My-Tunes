package com.example.mytunes.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.Player
import androidx.media3.common.Player.EVENT_MEDIA_ITEM_TRANSITION
import androidx.media3.common.Player.EVENT_TRACKS_CHANGED
import androidx.media3.session.MediaController
import com.example.mytunes.ui.viewModel.SongPlayerViewModel
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.guava.await

@Composable
fun BottomScreenPlayer(
    modifier: Modifier = Modifier,
    playerViewModel: SongPlayerViewModel,
    controllerFuture: ListenableFuture<MediaController>
) {
    var controller: MediaController
    val isPlaying = playerViewModel.isPlaying.collectAsState().value
    val listOfSong: List<String> = playerViewModel.queue.collectAsState().value.map {
        it.downloadUrl[2].url
    }
    val currentIndex = playerViewModel.currentIndex.collectAsState().value
    val currentSong = playerViewModel.currentSong.collectAsState().value




    HorizontalDivider(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        thickness = 1.dp
    )
    Box(modifier = modifier
        .fillMaxWidth()
        .height(64.dp)
        .background(
            color = MaterialTheme.colorScheme.surface,
        )
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                if (currentSong != null) {
                    CoverImage(
                            photo = currentSong.image[2].url,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(2.dp)
                                .size(60.dp)
                        )
                }
                if (currentSong != null) {
                    val allArtists = currentSong.artists.primary[0].name
                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .fillMaxHeight()
                            .align(Alignment.CenterVertically)
                            .width(180.dp)
                    ) {
                        Text(
                            text = removeParenthesesContent(currentSong.name.replace("&amp;", "and").replace("&quot;", "'")),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 12.sp,
                            color = Color.White
                        )
                        Text(
                            text = allArtists,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Gray,
                            lineHeight = 12.sp
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(160.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomPlayerIconFunction(
                    icon = Icons.Rounded.SkipPrevious,
                    playerViewModel = playerViewModel,
                    controllerFuture = controllerFuture
                )
                if (isPlaying) {
                    BottomPlayerIconFunction(
                        icon = Icons.Rounded.Pause,
                        playerViewModel = playerViewModel,
                        controllerFuture = controllerFuture
                    )
                } else {
                    BottomPlayerIconFunction(
                        icon = Icons.Rounded.PlayArrow,
                        playerViewModel = playerViewModel,
                        controllerFuture = controllerFuture
                    )
                }
                BottomPlayerIconFunction(
                    icon = Icons.Rounded.SkipNext,
                    playerViewModel = playerViewModel,
                    controllerFuture = controllerFuture
                )
            }
        }
    }
    HorizontalDivider(
        color = Color.White,
        thickness = 1.dp
    )

//    controllerFuture.addListener(
//        {
//            val a = controllerFuture.get()
//            a.addMediaItems(mediaItems)
//            a.seekTo(currentIndex, 0L)
//            a.prepare()
//            a.play()
//        },
//        MoreExecutors.directExecutor()
//    )
    LaunchedEffect(playerViewModel.currentIndex.collectAsState()) {

    }
}

@Composable
fun BottomPlayerIconFunction(
    icon: ImageVector,
    playerViewModel: SongPlayerViewModel,
    controllerFuture: ListenableFuture<MediaController>
) {
    val interactionSource = remember { MutableInteractionSource() }
    Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier
            .size(45.dp)
            .clickable(
                onClick = {
                    if (icon == Icons.Rounded.Pause) {
                        playerViewModel.pause()
                    } else if (icon == Icons.Rounded.PlayArrow) {
                        playerViewModel.play()
                    } else if (icon == Icons.Rounded.SkipPrevious) {
                        playerViewModel.playPreviousSong()
                    } else {
                        playerViewModel.playNextSong()
                    }
                },
                interactionSource = interactionSource,
                indication = null
            )
    )
}

