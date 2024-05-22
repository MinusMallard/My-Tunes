package com.example.mytunes.navigation

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.mytunes.AppViewModelProvider
import com.example.mytunes.ui.elements.items
import com.example.mytunes.ui.screen.GetLanguages
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
    sharedPre2: SharedPreferences
) {
    val currentTime = rememberSaveable { mutableStateOf(getCurrentTime()) }
    val greeting = rememberSaveable { mutableStateOf(getGreeting(currentTime.value.toString())) }


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
                            if (selectedIconIndex != index)navController.navigate(item.route){
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }

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
    ) {innerPadding ->
        NavHost (
            navController = navController,
            startDestination = "splashScreen",
            route = Graph.MAIN,
            modifier = modifier.padding(innerPadding)
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
                    navController = navController,
                    greeting = greeting.value,
                    name = sharedPre.getString("name", "")!!,
                    trendingNow = homeViewModel.trendingNow,
                    internationalCharts = homeViewModel.internationalCharts,
                    edmHot40 = homeViewModel.edmHot40,
                    romanticTop = homeViewModel.romanticTop40,
                    homePageData = homeViewModel.homePageData
                )
            }

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
    sharedPref2: SharedPreferences
) {
    NavHost(
        navController = navHostController,
        route = Graph.ROOT,
        startDestination = Graph.NAME
    ) {
        val name = sharedPre.getString("name", "").toString()
        val language = sharedPref2.getString("languages", "").toString()
        authNavGraph(navHostController, sharedPre, sharedPref2)
        composable(route = Graph.MAIN) {
            AppNavHost(sharedPre = sharedPre, sharedPre2 = sharedPref2)
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
