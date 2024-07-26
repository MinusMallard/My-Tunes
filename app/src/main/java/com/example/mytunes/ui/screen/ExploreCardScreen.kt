package com.example.mytunes.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.mytunes.model.Playlist
import com.example.mytunes.ui.elements.PlaylistCard
import com.example.mytunes.ui.viewModel.ExploreCardUiState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreCardScreen(
    paddingValues: Dp,
    modifier: Modifier = Modifier,
    category: String = "",
    color: Color,
    searchAlbum:(String) -> Unit,
    response: ExploreCardUiState,
    navigateTo: (String) -> Unit
) {
    LaunchedEffect(true) {
        searchAlbum(category)
    }
    Column(modifier = modifier.padding(bottom = paddingValues).fillMaxSize()) {
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
        Scaffold (
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = color,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        scrolledContainerColor = color
                    ),
                    modifier = Modifier,
                    title = {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Light,
                            fontSize = 36.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navigateTo("explore") },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onBackground
                            )
                            ) {
                            Icon (
                                imageVector = Icons.Filled.ArrowBackIosNew,
                                contentDescription = null
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { innerPadding ->
            when(response) {
                is ExploreCardUiState.Loading -> LoadingSearchScreen()
                is ExploreCardUiState.Error -> ErrorScreen()
                is ExploreCardUiState.Success -> ExploreCardContent(
                    albums = response.albums.data.results,
                    modifier = Modifier.padding(innerPadding),
                    navigateTo = navigateTo,
                )
            }
        }
    }
}

@Composable
fun ExploreCardContent(
    albums: List<Playlist>,
    modifier: Modifier = Modifier,
    navigateTo: (String) -> Unit
) {
    Box(modifier = modifier) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
        ) {
            items(albums) { album ->
                PlaylistCard(
                    album = album,
                    navigateTo = navigateTo
                )
            }
        }
    }
}



