package com.example.mytunes.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column {
            GithubProfile()
        }
    }
}

@Composable
fun GithubProfile() {
    val context = LocalContext.current
    var selected by remember { mutableIntStateOf(1) }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .weight(0.4f)
                        .padding(2.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "About the project",
                    fontSize = 24.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(8.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                HorizontalDivider(
                    modifier = Modifier
                        .weight(0.4f)
                        .padding(2.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                FilterChipExample(text = "About", onClick = { selected = 1 }, selected = selected == 1)
                FilterChipExample(
                    text = "Disclaimer",
                    onClick = { selected = 2 },
                    selected = selected == 2
                )
            }
        }
        item {
            AnimatedContent(
                targetState = selected, label = "about/disclaimer"
            ) {it ->
                when (it) {
                    1 -> {
                        Text(
                            text = "My Tunes leverages the extensive and diverse music library available through the unofficial JioSaavn API. This powerful integration allows users to explore a wide array of music genres, from Bollywood hits and regional classics to international chart-toppers. By tapping into JioSaavn's rich database, My Tunes ensures that users have access to a vast selection of songs, catering to varied musical tastes.\n" +
                                    "\n" +
                                    "The backbone of My Tunes' user interface and media handling capabilities is Jetpack Compose's Media3 library. This modern toolkit enables the creation of intuitive, responsive, and visually appealing user interfaces. With Media3, My Tunes delivers smooth media playback, efficient resource management, and an overall enhanced user experience.",
                            color = MaterialTheme.colorScheme.onBackground,
                            softWrap = true
                        )
                    }
                    2 -> {
                        Text(
                            text ="My Tunes does not possess or maintain any association with the songs and other content accessible through the app. All songs and other content are the property of their respective owners and are safeguarded by copyright law. My Tunes holds no liability for any copyright infringement or other violations of intellectual property rights that may arise from the use of the songs and other content accessible through the app. My Tunes employs third-party plugins and assumes no responsibility for any harm or damage to the respective owners or any other parties resulting from the utilization of the songs and other content through the third-party plugins. By using the app, you consent to utilizing the songs and other content exclusively for personal, non-commercial purposes and in accordance with all applicable laws and regulations.",
                            color = MaterialTheme.colorScheme.onBackground,
                            softWrap = true
                        )
                    }
                }
            }
        }
        item {
            val link = "https://github.com/MinusMallard/My-Tunes"
            TextButton(onClick = {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(link)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(context,intent, null)
            }) {
                Text(
                    text = "https://github.com/MinusMallard/My-Tunes",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}