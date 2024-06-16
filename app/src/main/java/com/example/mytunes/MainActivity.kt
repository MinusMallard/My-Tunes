package com.example.mytunes


import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.mytunes.media.PlaybackService
import com.example.mytunes.navigation.RootNavigationGraph
import com.example.mytunes.ui.theme.MyTunes
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import java.util.concurrent.Future


class MainActivity : ComponentActivity() {

    private lateinit var controllerFuture: ListenableFuture<MediaController>

    @RequiresApi(Build.VERSION_CODES.R)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
//        controllerFuture.addListener(
//            {
//                val a = controllerFuture.get()
//                val mediaItem = MediaItem.fromUri("https://aac.saavncdn.com/322/935ec81cd6eaccf69bec839dc72b0802_320.mp4")
//                a.setMediaItem(mediaItem)
//                a.prepare()
//                a.play()
//            },
//            MoreExecutors.directExecutor()

        super.onCreate(savedInstanceState)
        setContent {
            MyTunes {
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

    override fun onStop() {
        MediaController.releaseFuture(controllerFuture)
        super.onStop()
    }
}







