package com.example.mytunes.ui.elements

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mytunes.R

@Composable
fun CoverImage(
    photo: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
        .data(photo)
        .crossfade(true)
        .build(),

        contentDescription = "image",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(RoundedCornerShape(4.dp)),
        error = painterResource(R.drawable.download),
    )
}