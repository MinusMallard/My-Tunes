package com.example.mytunes

import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.mytunes.media.PlaybackService
import com.example.mytunes.navigation.RootNavigationGraph
import com.example.mytunes.ui.theme.MyTunesTheme
import com.google.common.util.concurrent.ListenableFuture

class MainActivity : ComponentActivity() {
    private lateinit var controllerFuture: ListenableFuture<MediaController>

    @RequiresApi(Build.VERSION_CODES.R)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        super.onCreate(savedInstanceState)
        setContent {

            MyTunesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
                    val sharedPreferences2 = context.getSharedPreferences("Languages", Context.MODE_PRIVATE)
                    RootNavigationGraph(sharedPre = sharedPreferences, sharedPref2 = sharedPreferences2, controllerFuture = controllerFuture)
                }
            }
        }
    }
}







