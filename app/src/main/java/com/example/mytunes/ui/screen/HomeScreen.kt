package com.example.mytunes.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mytunes.model.Song
import com.example.mytunes.ui.elements.HorizontalPlaylistSong
import com.example.mytunes.ui.elements.items


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    greeting: String,
    name: String,
    searchPlaylist:(String) -> Unit,

) {
    var selectedIconIndex by rememberSaveable {
        mutableIntStateOf(0)
    }
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.height(65.dp)
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        modifier = modifier,
                        selected = selectedIconIndex == index,
                        onClick = {
                            if (selectedIconIndex != index)navController.navigate(item.route)
                            selectedIconIndex = index },
                        icon = {
                            Icon(
                                imageVector = if (selectedIconIndex == index) item.selectedIcon
                                else item.unselectedIcon,
                                contentDescription = "null")
                        }
                    )
                }
            }
        }
    ){innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = modifier
                        .padding(16.dp)
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
            item {
                val link: String = "https://www.jiosaavn.com/featured/hindi-india-superhits-top-50/zlJfJYVuyjpxWb5,FqsjKg__"
                LaunchedEffect(Unit) {
                    searchPlaylist(link)
                }

            }
        }
    }
}