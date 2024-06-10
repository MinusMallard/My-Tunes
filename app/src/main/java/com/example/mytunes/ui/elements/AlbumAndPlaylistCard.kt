package com.example.mytunes.ui.elements

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mytunes.model.Album
import com.example.mytunes.model.Playlist

@Composable
fun AlbumCard(
    album: Album,
    modifier: Modifier = Modifier,
    navigateTo: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .clickable {
                navigateTo("album/${album.id}")
            }
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
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.width(200.dp)) {
        Column {
            Log.d("MainActivity", album.image[2].url.toString())
            CoverImage(photo = album.image[2].url.toString(), modifier = Modifier
                .size(200.dp)
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