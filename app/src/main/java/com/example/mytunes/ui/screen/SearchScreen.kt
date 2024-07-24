package com.example.mytunes.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mytunes.R
import com.example.mytunes.model.Album
import com.example.mytunes.model.AlbumResponse
import com.example.mytunes.model.Playlist
import com.example.mytunes.model.PlaylistResponse
import com.example.mytunes.model.SongResponse
import com.example.mytunes.ui.elements.AlbumCard
import com.example.mytunes.ui.elements.PlaylistCard
import com.example.mytunes.ui.elements.SongBanner
import com.example.mytunes.ui.theme.Shape

sealed interface SearchScreenState {
    data object Loading: SearchScreenState
    data object Nothing: SearchScreenState
    data class SuccessSong(
        val searchSongs: SongResponse
    ): SearchScreenState

    data class SuccessPlaylist(
        val searchPlaylists: PlaylistResponse
    ): SearchScreenState

    data class SuccessAlbum(
        val searchAlbums: AlbumResponse
    ): SearchScreenState

    data object Error: SearchScreenState
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    searchResponse:() -> Unit,
    response: SearchScreenState,
    resetResponse: () -> Unit,
    changeSearchType:(String) -> Unit,
    provideCategory:(String) -> Unit,
    provideColor:(Color) -> Unit,
    navigateTo: (String) -> Unit,
    searchType: String,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        Column(
            modifier
        ) {
            Row(

                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(visible = response is SearchScreenState.SuccessSong
                        || response is SearchScreenState.SuccessPlaylist
                        || response is SearchScreenState.SuccessAlbum
                        || response is SearchScreenState.Loading
                        || response is SearchScreenState.Error) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 16.dp, start = 16.dp, end = 10.dp)
                            .clickable(
                                onClick = {
                                    resetResponse()
                                    keyboardController?.hide()
                                },
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                indication = null
                            )
                    )
                }
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        onSearchTextChange(it)
                        searchResponse()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    placeholder = {
                        Text(
                            modifier = Modifier,
                            text = "Search",
                            color = Color.Gray,
                            fontSize = 18.sp
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    ),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,

                    ),
                    shape = MaterialTheme.shapes.medium
                )
            }
            Row {
                if (response is SearchScreenState.SuccessPlaylist
                    || response is SearchScreenState.SuccessSong
                    || response is SearchScreenState.SuccessAlbum
                    || response is SearchScreenState.Loading
                    || response is SearchScreenState.Error) {
                    FilterChipExample(
                        text = "songs",
                        onClick = {
                            changeSearchType("songs")
                        },
                        selected = searchType == "songs"
                    )
                    FilterChipExample(
                        text = "playlists",
                        onClick = {
                            changeSearchType("playlists")
                        },
                        selected = searchType == "playlists"
                    )
                    FilterChipExample(
                        text = "albums",
                        onClick = {
                            changeSearchType("albums")
                        },
                        selected = searchType == "albums"
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp)
            ) {
                when (response) {
                    is SearchScreenState.Nothing -> SearchScreenTiles(
                        provideCategory = provideCategory,
                        provideColor = provideColor,
                        navigateTo = navigateTo
                    )
                    is SearchScreenState.Loading -> LoadingSearchScreen()
                    is SearchScreenState.SuccessSong -> ResponseScreen(
                        response = response.searchSongs
                    )
                    is SearchScreenState.SuccessPlaylist -> PlaylistResponse(
                        playlist = response.searchPlaylists.data.results,
                        navController = navigateTo
                    )
                    is SearchScreenState.SuccessAlbum -> AlbumResponse(
                        album = response.searchAlbums.data.results,
                        navigateTo = navigateTo
                    )
                    else -> ErrorScreen()
                }
            }
        }
    }
}

@Composable
fun SearchScreenTiles(
    provideCategory:(String) -> Unit,
    provideColor:(Color) -> Unit,
    navigateTo: (String) -> Unit
) {

    Text(
        text = "Explore",
        modifier = Modifier.padding(8.dp),
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            ExploreBanners(
                provideCategory = provideCategory,
                provideColor = provideColor,
                navigateTo = navigateTo
            )
        }
        item {
            Explore(
                provideCategory = provideCategory,
                provideColor = provideColor,
                navigateTo = navigateTo,
                catStrings = mutableListOf("Hindi", "English", "Punjabi", "Tamil",
                    "Telugu", "Marathi", "Gujarati", "Bengali",
                    "Kannada", "Bhojpuri", "Malayalam", "Urdu",
                    "Haryanvi", "Rajasthani", "Odia", "Assamese"),
                cat = "Languages"
            )
        }
        item {
            Explore(provideCategory = provideCategory,
                provideColor = provideColor,
                navigateTo = navigateTo,
                catStrings = mutableListOf("Workout", "Chill", "Long Drive", "Late Night", "Focus", "Party", "Sleep", "Study" ),
                cat = "Moments"
            )
        }
        item {
            Explore(provideCategory = provideCategory,
                provideColor = provideColor,
                navigateTo = navigateTo,
                catStrings = mutableListOf("Hip Hop", "Retro", "K-pop", "Romance", "Jazz", "Classical", "Lofi" , "Sad"),
                cat = "Genres"
            )
        }
    }
}


@Composable
fun LoadingSearchScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun ResponseScreen(
    response: SongResponse
) {
    LazyColumn {
        items(response.data.results) { result ->
            SongBanner(
                photo = result.image[2].url,
                songName = result.name,
                artists = result.artists.all,
                onSongClick = {  },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ErrorScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "There was an error",
            modifier = Modifier.padding(8.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun FilterChipExample(
    text: String,
    onClick: () -> Unit,
    selected: Boolean,
) {
    FilterChip(
        modifier = Modifier
            .padding(8.dp)
            .animateContentSize(),
        onClick = {onClick()},
        label = {
            Text(text)
        },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}

@Composable
fun PlaylistResponse(
    playlist: List<Playlist>,
    navController: (String) -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(playlist) { it ->
            PlaylistCard(
                album = it,
                navigateTo = navController
            )
        }
    }
}

@Composable
fun AlbumResponse(
    album: List<Album>,
    navigateTo: (String) -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(album) { it ->
            AlbumCard(
                album = it,
                navigateTo = navigateTo
            )
        }
    }
}

@Composable
fun ExploreBanners(
    provideCategory:(String) -> Unit,
    provideColor:(Color) -> Unit,
    navigateTo: (String) -> Unit
) {
    val color = MaterialTheme.colorScheme.tertiaryContainer
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    Box(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            Row(
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            onClick = {
                                provideCategory("Trending")
                                provideColor(color)
                                navigateTo("card")
                            },
                            interactionSource = interactionSource,
                            indication = null
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.oig1),
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentDescription = "null",
                        alpha = 0.35f,
                        contentScale = ContentScale.Crop,

                        )
                    Text(
                        text = "Trending Now",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 18.sp,)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            onClick = {
                                provideCategory("Most Searched")
                                provideColor(color)
                                navigateTo("card")
                            },
                            interactionSource = interactionSource,
                            indication = null
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.oig3),
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentDescription = "null",
                        alpha = 0.35f,
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "Most Searched",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 18.sp,
                        )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
            ){
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            onClick = {
                                provideCategory("Pop Hits")
                                provideColor(color)
                                navigateTo("card")
                            },
                            interactionSource = interactionSource,
                            indication = null
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.oig1_rn14),
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentDescription = "null",
                        contentScale = ContentScale.Crop,
                        alpha = 0.35f
                    )
                    Text(
                        text = "Pop Hits",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 18.sp,
                        )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            onClick = {
                                provideCategory("Lofi")
                                provideColor(color)
                                navigateTo("card")
                            },
                            interactionSource = interactionSource,
                            indication = null
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.oig4_pxi6tftp7z),
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentDescription = "null",
                        alpha = 0.35f,
                        contentScale = ContentScale.Crop
                        )
                    Text(
                        text = "Lofi Beats",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 18.sp,
                        )
                }
            }
        }
    }
}

@Composable
fun Explore(
    provideCategory:(String) -> Unit,
    provideColor:(Color) -> Unit,
    navigateTo: (String) -> Unit,
    catStrings: MutableList<String>,
    cat: String
) {
    val color = MaterialTheme.colorScheme.primaryContainer
    Column {
        Text(
            text = cat,
            fontSize = 24.sp,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold
        )
        LazyHorizontalStaggeredGrid(
            rows = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .height(100.dp)
        ) {
            items(catStrings) {
                Button(
                    onClick = {
                        provideCategory(it)
                        provideColor(color)
                        navigateTo("card")
                    },
                    modifier = Modifier.padding(2.dp),
                    colors = ButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        disabledContentColor = MaterialTheme.colorScheme.onBackground,
                        disabledContainerColor = MaterialTheme.colorScheme.onBackground,
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = Shape.large
                ) {
                    Text(
                        text = it,
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}





