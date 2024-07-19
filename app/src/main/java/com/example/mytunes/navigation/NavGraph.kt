package com.example.mytunes.navigation

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.session.MediaController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mytunes.AppViewModelProvider
import com.example.mytunes.ui.elements.BottomScreenPlayer
import com.example.mytunes.ui.elements.items
import com.example.mytunes.ui.screen.AlbumScreen
import com.example.mytunes.ui.screen.ExploreCardScreen
import com.example.mytunes.ui.screen.GetLanguages
import com.example.mytunes.ui.screen.HomeScreen
import com.example.mytunes.ui.screen.GetName
import com.example.mytunes.ui.screen.LibraryScreen
import com.example.mytunes.ui.screen.PlayerScreen
import com.example.mytunes.ui.screen.PlaylistScreen
import com.example.mytunes.ui.screen.SearchScreen
import com.example.mytunes.ui.screen.SettingsScreen
import com.example.mytunes.ui.screen.SplashScreen
import com.example.mytunes.ui.viewModel.ExploreCardViewModel
import com.example.mytunes.ui.viewModel.HomePageLoadState
import com.example.mytunes.ui.viewModel.HomeViewModel
import com.example.mytunes.ui.viewModel.SearchViewModel
import com.example.mytunes.ui.viewModel.SongPlayerViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.common.util.concurrent.ListenableFuture
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@SuppressLint("ComposableDestinationInComposeScope", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    sharedPre: SharedPreferences,
    controllerFuture: ListenableFuture<MediaController>,
    sharedPre2: SharedPreferences
) {
    val systemUiController = rememberSystemUiController()
    if (isSystemInDarkTheme()) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false
        )
    } else {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }


    val currentTime = rememberSaveable { mutableStateOf(getCurrentTime()) }
    val greeting = rememberSaveable { mutableStateOf(getGreeting(currentTime.value)) }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: ""
    var selectedIconIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    if (currentRoute == "home") selectedIconIndex = 0
    else if (currentRoute == "explore") selectedIconIndex = 1

    val homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val homePageData by homeViewModel.homePageUiState.collectAsState()
    val ids: MutableList<String> = mutableListOf()
    when (homePageData) {
        is HomePageLoadState.Success -> {
            for (i in 0 until (homePageData as HomePageLoadState.Success).data.data.charts.size) {
                ids.add((homePageData as HomePageLoadState.Success).data.data.charts[i].id.toString())
            }
        }
        else -> {}
    }
    LaunchedEffect(ids) {
        homeViewModel.loadPlaylists(ids)
    }
    
    val playlists by homeViewModel.playlistsUiState.collectAsState()
    val searchViewModel: SearchViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val response by searchViewModel.response.collectAsState()
    val searchType: String = searchViewModel.searchType.collectAsState().value
    val playerViewModel: SongPlayerViewModel = viewModel(factory = AppViewModelProvider.Factory)

    var cardText by rememberSaveable {
        mutableStateOf("")
    }

    var cardColor by remember {
        mutableStateOf(Color.White)
    }

    val exploreCardViewModel: ExploreCardViewModel = viewModel(factory = AppViewModelProvider.Factory)
    Scaffold(
        bottomBar = {
            Column {
                BottomScreenPlayer(
                    playerViewModel = playerViewModel,
                    navigateTo = {navController.navigate("player")}
                )
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.height(90.dp)
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            modifier = modifier,
                            selected = selectedIconIndex == index,
                            onClick = {
                                navController.navigate(item.route)
                                selectedIconIndex = index
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selectedIconIndex == index) item.selectedIcon
                                    else item.unselectedIcon,
                                    contentDescription = "null"
                                )
                            }
                        )
                    }
                }
            }

        },
        contentWindowInsets = WindowInsets.statusBars
    ) {innerPadding ->
        NavHost (
            navController = navController,
            startDestination = "splashScreen",
            route = Graph.MAIN,
            modifier = modifier
        ) {
            composable(
                route = "splashScreen",
            ) {
                SplashScreen(openAndPopUp = { route, popUp->
                    navController.navigate(route) {
                        launchSingleTop = true
                        popUpTo(popUp) { inclusive = true}
                    }
                })
            }
            composable(
                route = "home",
            ) {
                HomeScreen(
                    greeting = greeting.value,
                    name = sharedPre.getString("name", "")!!,
                    homePageData = homePageData,
                    playlists = playlists,
                    controllerFuture = controllerFuture,
                    navigateTo = {
                        navController.navigate(it)
                    },
                    modifier = Modifier.padding(innerPadding),

                    getHomeContentData = homeViewModel::getAllPlayListSongs,
                    playerViewModel = playerViewModel,
                    scrollState = homeViewModel.scrollState
                )
            }
            composable(
                route = "explore",
            ) {
                val searchText by searchViewModel.searchText.collectAsState()
                SearchScreen(
                    searchText = searchText,
                    onSearchTextChange = {
                        searchViewModel.updateSearchString(it)
                    },
                    searchResponse = {searchViewModel.search()},
                    response = response,
                    resetResponse = {searchViewModel.reset()},
                    changeSearchType = {searchViewModel.changeSearchType(it)},
                    provideCategory = {
                        cardText = it
                    },
                    provideColor = {
                        cardColor = it
                    },
                    navigateTo = {
                        navController.navigate(it)
                    },
                    searchType = searchType,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            composable(
                route = "card"
            ) {
                val responseCard by exploreCardViewModel.uiState.collectAsState()
                ExploreCardScreen(
                    category = cardText,
                    color = cardColor,
                    searchAlbum = {
                        exploreCardViewModel.searchAlbums(it)
                                  },
                    response = responseCard,
                    navigateTo = {
                        navController.navigate(it)
                    }
                )
            }
            composable(
                route = "album/{albumId}",
                arguments = listOf(
                    navArgument("albumId"){ type = NavType.StringType }
                )
            ) { backStackEntry ->
                AlbumScreen(
                    modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                    id = backStackEntry.arguments?.getString("albumId"),
                    playerViewModel = playerViewModel
                )
            }

            composable(
                route = "playlist/{playlistId}",
                arguments = listOf(
                    navArgument("playlistId"){ type = NavType.StringType }
                )
            ) {
                PlaylistScreen(
                    modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                    id = it.arguments?.getString("playlistId"),
                    playerViewModel = playerViewModel
                )
            }
            composable(
                route = "settings"
            ) {
                SettingsScreen(
                    modifier = Modifier.padding(innerPadding),
                    navigateTo = {
                        navController.navigate(it){ popUpTo("settings") {inclusive = true} }
                    },
                    changeName = {
                        with (sharedPre.edit()) {
                            putString("name", it)
                            apply()
                        }
                        navController.navigate("home")
                    }
                )
            }

            composable(
                route = "getLanguages",
            ) {
                val context = LocalContext.current
                GetLanguages(
                    sharedPreference = sharedPre2,
                    navigateHome = {
                        navController.navigate("home") { popUpTo("getLanguages") {inclusive = true} }
                        Toast.makeText(
                            context,
                            "Language saved - Please restart the app",
                            Toast.LENGTH_LONG).show()
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            composable(
                route = "player",
            ) {
                PlayerScreen(
                    modifier = Modifier.padding(innerPadding),
                    playerViewModel = playerViewModel
                )
            }
            composable(
                route = "library"
            ) {
                LibraryScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(bottom = innerPadding.calculateBottomPadding())
                .height(300.dp)
                .fillMaxWidth()
        )
    }
}

private fun getCurrentTime(): String {
    val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return time.format(Date())
}

private fun getGreeting(time: String): String {
    return when (time.substringBefore(":").toInt()) {
        in 6..11 -> "Good morning"
        in 12..17 -> "Good afternoon"
        else -> "Good evening"
    }
}

object Graph {
    const val ROOT = "root"
    const val NAME = "name"
    const val MAIN = "main_graph"
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun RootNavigationGraph(
    navHostController: NavHostController = rememberNavController(),
    sharedPre: SharedPreferences,
    sharedPref2: SharedPreferences,
    controllerFuture: ListenableFuture<MediaController>
) {
    val name = sharedPre.getString("name", "").toString()
    val language = sharedPref2.getString("languages", "").toString()
    NavHost(
        navController = navHostController,
        route = Graph.ROOT,
        startDestination =
            if (name.isEmpty() || language.isEmpty()) {
                Graph.NAME
            } else {
                Graph.MAIN
            }
    ) {
        authNavGraph(navHostController, sharedPre, sharedPref2)
        composable(route = Graph.MAIN) {
            AppNavHost(
                sharedPre = sharedPre,
                controllerFuture = controllerFuture,
                sharedPre2 = sharedPref2
            )
        }
    }
}


fun NavGraphBuilder.authNavGraph (navController: NavHostController, sharedPre: SharedPreferences, sharedPre2: SharedPreferences) {
    navigation(
        route = Graph.NAME,
        startDestination = "getName"
    ) {
        composable(
            route = "getName",
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) }
        ) {
            GetName(
                sharedPref = sharedPre,
                navigateGetLang = {navController.navigate("getLanguages"){ popUpTo("getName") {inclusive = true} } }
            )
        }

        composable(
            route = "getLanguages",
            enterTransition = { fadeIn(animationSpec = tween(durationMillis = 500))},
            exitTransition = { fadeOut(animationSpec = tween(durationMillis = 50))}
        ) {
            GetLanguages(
                sharedPreference = sharedPre2,
                navigateHome = {navController.navigate(Graph.MAIN) { popUpTo("getLanguage") {inclusive = true} } }
            )
        }
    }
}
