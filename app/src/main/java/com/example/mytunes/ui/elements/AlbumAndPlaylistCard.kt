package com.example.mytunes.ui.elements

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mytunes.model.Album
import com.example.mytunes.model.Playlist

@Composable
fun AlbumCard(
    album: Album,
    navigateTo: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource()}
    Box(
        modifier = Modifier
            .width(200.dp)
            .clickable ( onClick = {
                navigateTo("album/${album.id}")
            },
                interactionSource = interactionSource,
                indication = null
            )
            .padding(8.dp)
    ) {
        Column {
            Log.d("MainActivity", album.image[2].url.toString())
            CoverImage(photo = album.image[2].url.toString(), modifier = Modifier
                .size(200.dp)
                .padding(start = 8.dp,top = 4.dp, end = 8.dp, bottom = 8.dp)
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
        }
    }
}

@Composable
fun PlaylistCard(
    album: Playlist,
    navigateTo: (String) -> Unit
) {
    val interactionSource = remember{ MutableInteractionSource()}
    Box(
        modifier = Modifier.width(200.dp)
            .clickable ( onClick = {
                    navigateTo("playlist/${album.id}")
                },
                interactionSource = interactionSource,
                indication = null
            )
            .padding(8.dp)
    ) {
        Column {
            Log.d("MainActivity", album.image[2].url.toString())
            CoverImage(photo = album.image[2].url.toString(), modifier = Modifier
                .size(180.dp)
                .padding(start = 8.dp,top = 4.dp, end = 8.dp, bottom = 8.dp)
            )
            album.name?.let {
                Text(
                    text = it,
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