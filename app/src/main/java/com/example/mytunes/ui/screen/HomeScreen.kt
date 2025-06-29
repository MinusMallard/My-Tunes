package com.example.mytunes.ui.screen

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.session.MediaController
import com.example.mytunes.model.Data
import com.example.mytunes.model.PlaylistApiResponse
import com.example.mytunes.model.Song
import com.example.mytunes.ui.elements.HorizontalAlbums
import com.example.mytunes.ui.elements.HorizontalPlaylist
import com.example.mytunes.ui.elements.HorizontalSongBanner
import com.example.mytunes.ui.viewModel.HomePageLoadState
import com.example.mytunes.ui.viewModel.PlaylistsUiState
import com.example.mytunes.ui.viewModel.SongPlayerViewModel
import com.google.common.util.concurrent.ListenableFuture

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    greeting: String,
    name: String,
    homePageData: HomePageLoadState,
    navigateTo: (String) -> Unit,
    getHomeContentData:() -> Unit,
    playlists: PlaylistsUiState,
    playerViewModel: SongPlayerViewModel,
    scrollState: LazyListState,
) {

    val songs = playerViewModel.queue.collectAsStateWithLifecycle()
    val songIndex = playerViewModel.currentIndex.collectAsStateWithLifecycle()
    var playlists1: MutableList<PlaylistApiResponse> = mutableListOf()
    when(playlists) {
        is PlaylistsUiState.Success -> playlists1 = playlists.playlist
        else -> {}
    }
    val interactionSource = remember { MutableInteractionSource() }
    Box(modifier = modifier.fillMaxSize()) {
        when(homePageData) {
            is HomePageLoadState.Success -> HomeContent(
                name = name,
                greeting = greeting,
                navigateTo = navigateTo,
                homePageData = homePageData.data.data,
                playlists = playlists1,
                playerViewModel = playerViewModel,
                scrollState = scrollState,
                songId = if (songs.value.isNotEmpty()) songs.value[songIndex.value].id else null,
                )
            is HomePageLoadState.Loading -> LoadingSearchScreen()
            else -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "There Was An Error",
                        modifier = Modifier.padding(8.dp),
                        fontWeight = FontWeight.W400,
                        fontSize = 24.sp
                    )
                    IconButton(
                        onClick = { getHomeContentData() },
                        modifier = Modifier
                            .indication(interactionSource = interactionSource, indication = null)
                            .size(50.dp)
                            .padding(8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Replay,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .indication(
                                    interactionSource = interactionSource, indication = null
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greetings(
    name: String,
    greeting: String,
    navigateTo: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(start = 4.dp, bottom = 24.dp, top = 16.dp)
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
        IconButton(onClick = {navigateTo("settings")}) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "settings"
            )
        }
    }
}


// This Composable is responsible for showing everything that you see
// on screen the implementation could be better but it's fine
@Composable
fun HomeContent(
    name: String,
    greeting: String,
    navigateTo: (String) -> Unit,
    homePageData: Data,
    playlists: MutableList<PlaylistApiResponse>,
    playerViewModel: SongPlayerViewModel,
    scrollState: LazyListState,
    songId: String?,
) {
    val halfSizePlaylist = homePageData.playlists.size / 2
    var index by rememberSaveable{
        mutableIntStateOf(0)
    }

    LazyColumn(
        state = scrollState,
    ) {

        item {
                Greetings(name = name, greeting = greeting, navigateTo = navigateTo)
            }
        item {
            if (0 < playlists.size) {
                HorizontalSongBanner(
                    songList = playlists[0].data.songs,
                    name = playlists[0].data.name,
                    onSongClick = { songs: List<Song>, song: Song->
                        playerViewModel.setCurrentIndex(songs.indexOf(song))
                        playerViewModel.addSongList(songs.toMutableList())
                    },
                    songId = songId.toString()
                )
                index = 1
            }
        }
        item {
            HorizontalPlaylist(
                playlists = homePageData.playlists.subList(0, halfSizePlaylist),
                name = "Recommended Playlist",
                navigateTo = navigateTo,
            )
        }
        item {
            if (1 < playlists.size) {
                HorizontalSongBanner(
                    songList = playlists[1].data.songs,
                    name = playlists[1].data.name,
                    onSongClick = { songs: List<Song>, song: Song->
                        playerViewModel.setCurrentIndex(songs.indexOf(song))
                        playerViewModel.addSongList(songs.toMutableList())
                    },
                    songId = songId.toString())
                index = 2
            }
        }
        item {
            HorizontalAlbums(
                albums = homePageData.albums.subList(0, homePageData.albums.size-1),
                name = "Recommended Albums",
                navigateTo = navigateTo
            )
        }
        item {
            if (2 < playlists.size) {
                HorizontalSongBanner(
                    songList = playlists[2].data.songs,
                    name = playlists[2].data.name,
                    onSongClick = { songs: List<Song>, song: Song->
                        playerViewModel.setCurrentIndex(songs.indexOf(song))
                        playerViewModel.addSongList(songs.toMutableList())
                    },
                    songId = songId.toString())
                index = 3
            }
        }
        item {
            HorizontalPlaylist(
                playlists = homePageData.playlists.subList(halfSizePlaylist, homePageData.playlists.size),
                name = "Top Playlists",
                navigateTo = navigateTo,
            )
        }
        item {
            if (3 < playlists.size) {
                HorizontalSongBanner(
                    songList = playlists[3].data.songs,
                    name = playlists[3].data.name,
                    onSongClick = {songs: List<Song>, song: Song->
                        playerViewModel.setCurrentIndex(songs.indexOf(song))
                        playerViewModel.addSongList(songs.toMutableList())
                    },
                    songId = songId.toString()
                )
                index =4
            }
        }
        item {
            if (4 < playlists.size) {
                HorizontalSongBanner(
                    songList = playlists[4].data.songs,
                    name = playlists[4].data.name,
                    onSongClick = {songs: List<Song>, song: Song->
                        playerViewModel.setCurrentIndex(songs.indexOf(song))
                        playerViewModel.addSongList(songs.toMutableList())

                    },
                    songId = songId.toString())
                index = 5
            }
        }
    }
}


