package com.example.mytunes.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.session.MediaController
import com.example.mytunes.model.HomeAlbum
import com.example.mytunes.model.HomePlaylist
import com.google.common.util.concurrent.ListenableFuture

@Composable
fun HorizontalAlbums(
    albums: List<HomeAlbum>,
    name: String,
    controllerFuture: ListenableFuture<MediaController>,
    navigateTo: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box (
        modifier = Modifier
            .padding(top = 24.dp, bottom = 8.dp)
    ) {
        Column {
            Text(
                text = name,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp,bottom = 4.dp),
                fontWeight = FontWeight.Bold
            )
            LazyHorizontalGrid(
                modifier = Modifier.heightIn(max=240.dp),
                rows = GridCells.Fixed(1)
            ) {
                items(albums) { album ->
                    Box(modifier = Modifier.width(200.dp)) {
                        var artist: String = ""
                        for (item in album.primaryArtists) {
                            val nameArt = item.name
                            artist += if (artist == "") nameArt
                            else ", $nameArt"
                        }
                        Column {
                            CoverImage(photo = album.image[2].link, modifier = Modifier
                                .size(200.dp)
                                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 8.dp)
                                .clickable ( onClick = {
                                    navigateTo("album/${album.id}")
                                },
                                    interactionSource = interactionSource,
                                    indication = null
                                )
                            )
                            Text(
                                text = album.name,
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 12.sp
                            )
                            Text(
                                text = artist,
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                                fontWeight = FontWeight.Light,
                                fontSize = 12.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.Gray,
                                lineHeight = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HorizontalPlaylist(
    playlists: List<HomePlaylist>,
    name: String,
    controllerFuture: ListenableFuture<MediaController>,
    navigateTo: (String) -> Unit
) {
    val interactionSource = remember {MutableInteractionSource()}
    Box (
        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
    ) {
        Column {
            Text(
                text = name,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp,bottom = 4.dp),
                fontWeight = FontWeight.Bold
            )
            LazyHorizontalGrid(
                modifier = Modifier.heightIn(max=240.dp),
                rows = GridCells.Fixed(1)
            ) {
                items(playlists) { playlist ->
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .clickable ( onClick = {
                                navigateTo("playlist/${playlist.id}")
                            },
                                interactionSource = interactionSource,
                                indication = null
                            )
                    ) {
                        Column {
                            CoverImage(photo = playlist.image[2].link.toString(), modifier = Modifier
                                .size(200.dp)
                                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 8.dp)
                            )
                            Text(
                                text = playlist.title,
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}