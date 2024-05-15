package com.example.mytunes.navigation

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@SuppressLint("ComposableDestinationInComposeScope")
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    sharedPre: SharedPreferences,
) {
    val currentTime = rememberSaveable { mutableStateOf(getCurrentTime()) }
    val greeting = rememberSaveable { mutableStateOf(getGreeting(currentTime.value.toString())) }

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
            val homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
            HomeScreen(
                isLoaded = homeViewModel.isLoaded,
                navController = navController,
                greeting = greeting.value,
                name = sharedPre.getString("name", "")!!,
                searchPlaylist = {homeViewModel.getPlayList(it)},
//                songs = homeViewModel.playListLoadState
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