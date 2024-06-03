package com.example.mytunes.ui.screen

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.navigation.NavHostController
import com.example.mytunes.ui.elements.HorizontalAlbums
import com.example.mytunes.ui.elements.HorizontalSongBanner
import com.example.mytunes.ui.viewModel.HomePageLoadState
import com.example.mytunes.ui.viewModel.PlayListLoadState
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import java.util.concurrent.Future


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    isLoaded: Boolean = false,
    navController: NavHostController,
    greeting: String,
    name: String,
    trendingNow: PlayListLoadState,
    internationalCharts: PlayListLoadState,
    edmHot40: PlayListLoadState,
    romanticTop: PlayListLoadState,
    homePageData: HomePageLoadState,
    controllerFuture: ListenableFuture<MediaController>
) {
    var index by rememberSaveable {
        mutableStateOf<Int?>(null)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .animateContentSize()
        ) {
            item {
                Greetings(name = name, greeting = greeting, modifier = modifier)
            }
            item {
                when (trendingNow) {
                    is PlayListLoadState.Success -> HorizontalSongBanner(
                        songList = trendingNow.songs,
                        name = trendingNow.name,
                        controllerFuture = controllerFuture,
                        onSongClick = {
                            index = it
                            var mediaItems: MutableList<MediaItem> = mutableListOf()
                            controllerFuture.addListener(
                                {
                                    trendingNow.songs.forEach{ it ->
                                        mediaItems.add(MediaItem.fromUri(it.downloadUrl[2].url))
                                    }
                                    val a = controllerFuture.get()
                                    a.setMediaItems(mediaItems, index!!, 0)
                                    a.prepare()
                                    a.play()
                                },
                                MoreExecutors.directExecutor()
                            )
                        }
                    )

                    is PlayListLoadState.Loading -> {
                        LoadingScreen()
                    }

                    is PlayListLoadState.Error -> {
                        Log.d("MainActivity", "error2")
                    }
                }
                var halfSize = 0
                when (homePageData) {
                    is HomePageLoadState.Success -> {
                        halfSize = homePageData.data.data.albums.size / 2
                        HorizontalAlbums(
                            albums = homePageData.data.data.albums.subList(
                                0,
                                halfSize
                            ),
                            name = "Recommended Albums",
                            controllerFuture = controllerFuture
                        )
                        Log.d("home", homePageData.data.data.albums.subList(0, halfSize).toString())
                    }
                    else -> {}
                }
                when (edmHot40) {
                    is PlayListLoadState.Success -> HorizontalSongBanner(
                        songList = edmHot40.songs,
                        name = edmHot40.name,
                        controllerFuture = controllerFuture,
                        onSongClick = {
                            index = it
                            var mediaItems: MutableList<MediaItem> = mutableListOf()
                            controllerFuture.addListener(
                                {
                                    edmHot40.songs.forEach{ it ->
                                        mediaItems.add(MediaItem.fromUri(it.downloadUrl[2].url))
                                    }
                                    val a = controllerFuture.get()
                                    a.setMediaItems(mediaItems, index!!, 0)
                                    a.prepare()
                                    a.play()
                                },
                                MoreExecutors.directExecutor()
                            )
                        }
                    )

                    is PlayListLoadState.Loading -> LoadingScreen()
                    is PlayListLoadState.Error -> {}
                }

                when (homePageData) {
                    is HomePageLoadState.Success -> {
                        var size = homePageData.data.data.albums.size
                        Log.d("HOME", size.toString())
                        HorizontalAlbums(
                            albums = homePageData.data.data.albums.subList(
                                halfSize,
                                size
                            ),
                            name = "Recommended",
                            controllerFuture = controllerFuture
                        )
                    }

                    is HomePageLoadState.Loading -> {
                        Log.d("MainActivity", "This is a debug log message1")
                    }

                    is HomePageLoadState.Error1 -> {
                        Log.d("MainActivity", "This is a debug log message2")
                    }

                    is HomePageLoadState.Error2 -> {
                        Log.d("MainActivity", "This is a debug log message3")
                    }

                    is HomePageLoadState.Error3 -> {
                        Log.d("MainActivity", "This is a debug log message4")
                    }

                    is HomePageLoadState.Error4 -> {
                        Log.d("MainActivity", "This is a debug log message5")
                    }
                }
                Log.d("index", index.toString())
                when (romanticTop) {
                    is PlayListLoadState.Success -> HorizontalSongBanner(
                        songList = romanticTop.songs,
                        name = romanticTop.name,
                        controllerFuture = controllerFuture,
                        onSongClick = {
                            index = it
                            var mediaItems: MutableList<MediaItem> = mutableListOf()
                            controllerFuture.addListener(
                                {
                                    romanticTop.songs.forEach{ it ->
                                        mediaItems.add(MediaItem.fromUri(it.downloadUrl[2].url))
                                    }
                                    val a = controllerFuture.get()
                                    a.setMediaItems(mediaItems, index!!, 0)
                                    a.prepare()
                                    a.play()
                                },
                                MoreExecutors.directExecutor()
                            )
                        }
                    )

                    is PlayListLoadState.Loading -> LoadingScreen()
                    is PlayListLoadState.Error -> {}
                }
            }
        }
    }

}

@Composable
fun Greetings(
    name: String,
    greeting: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(start = 4.dp, bottom = 16.dp, top = 16.dp)
            .fillMaxWidth()
    ) {
        Column{
            Text(
                text = "Hey, $name",
                fontWeight = FontWeight.Thin,
                fontSize = 12.sp
            )
            Text(
                text = greeting,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "settings"
            )
        }
    }
}

@Composable
fun LoadingScreen() {}

