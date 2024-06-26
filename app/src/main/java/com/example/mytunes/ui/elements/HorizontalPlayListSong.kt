package com.example.mytunes.ui.elements

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.session.MediaController
import com.example.mytunes.model.Artist
import com.example.mytunes.model.DownloadUrl
import com.example.mytunes.model.Song
import com.google.common.util.concurrent.ListenableFuture
@Composable
fun HorizontalSongBanner(
    songList: List<Song>,
    name :String,
    controllerFuture: ListenableFuture<MediaController>,
    onSongClick: (List<Song>, Song)-> Unit,

) {
    var index by rememberSaveable {
        mutableStateOf<Int?>(null)
    }
    Box(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)) {
        Column {
            Text(
                text = name,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 4.dp,bottom = 4.dp),
                fontWeight = FontWeight.Bold
            )
            LazyHorizontalGrid(
                modifier = Modifier.heightIn(max=250.dp),
                rows = GridCells.Fixed(5)) {
                items(songList) { song ->
                    SongBanner(
                        modifier = Modifier.width(240.dp),
                        photo = song.image[1].url,
                        songName = song.name,
                        artists = song.artists.primary,
                        controllerFuture = controllerFuture,
                        onSongClick = {
                            onSongClick(songList, song)
                        },
                        songUrl = song.downloadUrl[2]
                    )
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun SongBanner(
    modifier: Modifier = Modifier,
    photo: String,
    songName: String,
    artists: List<Artist>,
    controllerFuture: ListenableFuture<MediaController>,
    onSongClick:() -> Unit,
    songUrl: DownloadUrl,
) {
    val allArtists = artists[0].name.replace("&amp;", "and").replace("&quot;", "'")
    var isSongPlaying by rememberSaveable {
        mutableStateOf(false)
    }
    var color = if (isSongPlaying) MaterialTheme.colorScheme.primary
    else Color.White
    Log.d("color", isSongPlaying.toString())
    Row(
        modifier = modifier
            .padding(4.dp)
            .height(50.dp)

            .clickable(
                onClick = {
                    onSongClick()
                },
                interactionSource = MutableInteractionSource(),
                indication = null
            )
    ) {
        CoverImage(photo = photo, modifier = Modifier
            .height(50.dp)
            .width(50.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = removeParenthesesContent(songName.replace("&amp;", "and").replace("&quot;", "'")),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 12.sp,
                color = color
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

fun removeParenthesesContent(input: String): String {
    return input.replace(Regex("\\([^)]*\\)"), "")
}
