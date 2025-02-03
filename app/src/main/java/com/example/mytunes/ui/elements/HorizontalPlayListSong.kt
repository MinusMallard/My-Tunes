package com.example.mytunes.ui.elements

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mytunes.model.Artist
import com.example.mytunes.model.Song
@Composable
fun HorizontalSongBanner(
    songList: List<Song>,
    name :String,
    onSongClick: (List<Song>, Song)-> Unit,
    songId: String = ""
) {

    Box(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)) {
        Column {
            Text(
                text = name,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 4.dp,bottom = 4.dp),
                fontWeight = FontWeight.Bold
            )
            LazyHorizontalGrid(
                modifier = Modifier.heightIn(max=280.dp),
                rows = GridCells.Fixed(5),
            ) {
                items(songList) { song ->
                    SongBanner(
                        modifier = Modifier.width(240.dp),
                        photo = song.image[1].url,
                        songName = song.name,
                        artists = song.artists.primary,
                        onSongClick = {
                            onSongClick(songList, song)
                        },
                        songId = songId,
                        song = song
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
    onSongClick:() -> Unit,
    songId: String = "",
    song: Song? = null
) {
    var allArtists = ""
    for (artist in artists) {
        allArtists += artist.name.replace("&amp;", "and").replace("&quot;", "'")
        if (artists.indexOf(artist) != artists.size - 1) {
            allArtists += ", "
        }
    }
    val isSongPlaying by rememberSaveable {
        mutableStateOf(false)
    }
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
        val infiniteTransition = rememberInfiniteTransition(label = "rotate image")
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
        CoverImage(
            photo = photo,
            modifier = if (songId == song?.id)
                Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .clip(CircleShape)
                    .graphicsLayer {
                        rotationZ = rotateAnim
                        transformOrigin = TransformOrigin.Center
                    }.animateContentSize()
            else Modifier.height(50.dp).width(50.dp).clip(CircleShape) )
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
                    color =  if (song?.id == songId) Color.Green else Color.White
                )

            Text(
                text = allArtists,
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

fun removeParenthesesContent(input: String): String {
    return input.replace(Regex("\\([^)]*\\)"), "")
}
