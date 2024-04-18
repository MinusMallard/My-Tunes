package com.example.mytunes.navigation

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mytunes.AppViewModelProvider
import com.example.mytunes.ui.screen.HomeScreen
import com.example.mytunes.ui.screen.GetName
import com.example.mytunes.ui.screen.SplashScreen
import com.example.mytunes.ui.viewModel.HomeViewModel


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@SuppressLint("ComposableDestinationInComposeScope")
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    sharedPre: SharedPreferences,
    greeting: String
) {
    val homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
    NavHost (
        navController = navController,
        startDestination = "splashScreen",
        modifier = modifier
    ) {

        composable(
            route = "splashScreen",
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) }
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
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) }
        ) {
            HomeScreen(
                navController = navController,
                greeting = greeting,
                name = sharedPre.getString("name", "")!!,
                searchPlaylist = {homeViewModel.getPlayList(it)},
                songs = homeViewModel.playListLoadState
                )
        }
        composable(
            route = "getName",
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) }
        ) {
            GetName(sharedPref = sharedPre, navigateHome = {navController.navigate("home"){
                popUpTo("getName") {inclusive = true}
            }
            })
        }
    }
}