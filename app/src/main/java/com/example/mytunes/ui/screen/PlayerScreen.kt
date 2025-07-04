package com.example.mytunes.ui.screen

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mytunes.ui.elements.CoverImage
import com.example.mytunes.ui.elements.SongBanner
import com.example.mytunes.ui.viewModel.SongPlayerViewModel
import com.google.android.material.slider.Slider


@OptIn(ExperimentalMaterial3Api::class)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    playerViewModel: SongPlayerViewModel,
    navController: NavHostController
) {
    val currentSong = playerViewModel.currentSong.collectAsState().value
    val isPlaying = playerViewModel.isPlaying.collectAsState().value
    val progress = playerViewModel.progress.collectAsState().value
    val queue = playerViewModel.queue.collectAsState().value
    
    val infiniteTransition = rememberInfiniteTransition(label = "rotate image")

    // Animation for rotating thumbnail
    val rotateAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 30000,
                easing = LinearEasing,
            ),
        ), label = "rotation"
    )
    if (currentSong!= null) {
        AnimatedContent(
            targetState = currentSong,
            label = "song",
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }
        ) {
            CoverImage(
                photo = it.image[2].url,
                modifier = Modifier
                    .blur(
                        radiusX = 20.dp,
                        radiusY = 20.dp,
                        edgeTreatment = BlurredEdgeTreatment.Rectangle
                    )
                    .fillMaxWidth()
            )
        }
    }
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = { navController.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(200.dp))
                            .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.4f), shape = RoundedCornerShape(50.dp)),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            if (currentSong != null) {
                CoverImage(
                    photo = currentSong.image[2].url,
                    modifier = Modifier
                        .clip(RoundedCornerShape(200.dp))
                        .size(350.dp)
                        .graphicsLayer {
                            rotationZ = rotateAnim
                            transformOrigin = TransformOrigin.Center
                        }
                        .animateContentSize()
                )
            }

            Column (
                modifier = Modifier
                    .padding(20.dp)
                    .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.4f), shape = RoundedCornerShape(25.dp))
            ) {
                if (currentSong != null) {
                    Text(
                        text = currentSong.name,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 20.dp),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                SliderView(
                    progress = progress,
                    seekTo = {playerViewModel.seekTo(it)}
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(
                        onClick = {
                            playerViewModel.playPreviousSong()
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.SkipPrevious,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(200.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = {
                            if (isPlaying) {
                                playerViewModel.pause()
                            } else {
                                playerViewModel.play()
                            }
                        },
                        modifier = Modifier.size(60.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(200.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(
                        onClick = {
                            playerViewModel.playNextSong()
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.SkipNext,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(200.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            SongsInQueue()
        }
    }
    val scaffoldState = rememberBottomSheetScaffoldState()
}

@Composable
fun SliderView(
    progress: Float,
    modifier: Modifier = Modifier,
    seekTo: (Float) -> Unit
) {
    Slider(
        value = progress,
        onValueChange = {
            seekTo(it) },
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(1.dp),
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.secondary,
            activeTrackColor = MaterialTheme.colorScheme.primary,
            activeTickColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

@Composable
fun SongsInQueue(

) {
    Column (
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp).fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.4f), shape = RoundedCornerShape(25.dp))
    ) {

    }
}
