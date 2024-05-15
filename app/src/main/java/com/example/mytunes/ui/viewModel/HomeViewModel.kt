package com.example.mytunes.ui.viewModel

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytunes.data.MyTunesDataRepository
import com.example.mytunes.model.Song
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(
    private val myTunesDataRepository: MyTunesDataRepository
): ViewModel(){


    var isLoaded = false                        // for checking whether elements of home screen are already loaded or not
        private set

    fun changeIsLoaded() {
        isLoaded = true
    }
    var playListLoadState: PlayListLoadState by mutableStateOf(PlayListLoadState.Loading)
        private set

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getPlayList(a: String) {
        playListLoadState = PlayListLoadState.Loading
        viewModelScope.launch {
            playListLoadState = try {
                val response = myTunesDataRepository.getPlaylist(a, 1, 25)
                if (!response.success) {
                    PlayListLoadState.Error
                } else {
                    PlayListLoadState.Success (
                        songs = response.data.songs
                    )
                }
            } catch (e: IOException) {
                PlayListLoadState.Error
            } catch (e: HttpException) {
                PlayListLoadState.Error
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}

sealed interface PlayListLoadState {
    data class Success(val songs: List<Song>): PlayListLoadState
    object Loading: PlayListLoadState
    object Error: PlayListLoadState
}

data class Links(
    val trendingNow: String = " https://www.saavn.com/s/featured/kannada/trending-today/I3kvhipIy73uCJW60TJk1Q__?referrer=svn_source=share&svn_medium=com.whatsapp&utm_source=share&utm_medium=com.whatsapp ",
    val internationalCharts: String = "https://www.jiosaavn.com/featured/international_charts/DjLfyo0wfbk_",
    val edmHot40: String = "https://www.jiosaavn.com/featured/edm_hot_40/m9Qkal5S733uCJW60TJk1Q__",
    val romanticTop40: String = "https://www.jiosaavn.com/featured/romantic_top_40/m9Qkal5S733ufxkxMEIbIw__"
)