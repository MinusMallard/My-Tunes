package com.example.mytunes.ui.screen

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mytunes.AppViewModelProvider
import com.example.mytunes.ui.viewModel.SplashViewModel
import kotlinx.coroutines.delay

private const val SPLASH_TIMEOUT = 1000L

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun SplashScreen(
    openAndPopUp: (String,String) -> Unit,
    modifier: Modifier = Modifier
    ) {

    val viewModel: SplashViewModel = viewModel(factory = AppViewModelProvider.Factory)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
    }

    LaunchedEffect(true) {
        delay(SPLASH_TIMEOUT)
        viewModel.onStart(openAndPopUp)
    }
}