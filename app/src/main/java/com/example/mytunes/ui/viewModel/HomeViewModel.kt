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
}

sealed interface PlayListLoadState {
    data class Success(val songs: List<Song>): PlayListLoadState
    object Loading: PlayListLoadState
    object Error: PlayListLoadState
}