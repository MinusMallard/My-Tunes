package com.example.mytunes.ui.viewModel

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytunes.data.MyTunesDataRepository
import com.example.mytunes.model.HomeApiResponse
import com.example.mytunes.model.PlaylistApiResponse
import com.example.mytunes.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class HomeViewModel(
    private val myTunesDataRepository2: MyTunesDataRepository,
    private val myTunesDataRepository: MyTunesDataRepository,
    private val languages: String
): ViewModel(){

    private val _homePageUiState = MutableStateFlow<HomePageLoadState>(HomePageLoadState.Loading)
    val homePageUiState: StateFlow<HomePageLoadState> = _homePageUiState.asStateFlow()

    private val _playlistsUiState = MutableStateFlow<PlaylistsUiState>(PlaylistsUiState.Loading)
    val playlistsUiState: StateFlow<PlaylistsUiState> = _playlistsUiState.asStateFlow()

    init {
        getAllPlayListSongs()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getAllPlayListSongs() {
        _homePageUiState.value = HomePageLoadState.Loading
        viewModelScope.launch {
            Log.d("Lang", languages)
            _homePageUiState.update {
                try {
                    val load = myTunesDataRepository2.getHomePageData(languages)
                    Log.d("new", load.data.playlists.toString())
                    Log.d("new", load.data.charts.toString())
                    Log.d("new", load.data.albums.toString())
                    Log.d("new", load.data.trending.toString())
                    if (load.status != "SUCCESS") {
                        HomePageLoadState.Error1
                    } else {
                        HomePageLoadState.Success(
                            data = load
                        )
                    }
                }
                catch (e: IOException) {
                    HomePageLoadState.Error2
                } catch (e: HttpException) {
                    HomePageLoadState.Error3
                } catch (e: Exception) {
                    HomePageLoadState.Error4
                }

            }
        }
    }

    fun loadPlaylists(id: List<String>) {
        val playlists = mutableListOf<PlaylistApiResponse>()
        viewModelScope.launch {
            for (i in id) {
                _playlistsUiState.update {
                    try {
                        val response = myTunesDataRepository.getPlaylist(i)
                        if (response.success) {
                            playlists.add(response)
                            PlaylistsUiState.Success(playlists)
                        } else {
                            PlaylistsUiState.Error
                        }
                    } catch (e: Exception) {
                        PlaylistsUiState.Error
                    }
                }
            }
            Log.d("Playlists", playlists.toString())
        }
    }

}


sealed interface HomePageLoadState {
    data class Success(val data: HomeApiResponse): HomePageLoadState

    data object Loading: HomePageLoadState
    data object Error1: HomePageLoadState
    data object Error2: HomePageLoadState
    data object Error3: HomePageLoadState
    data object Error4: HomePageLoadState
}

sealed interface PlayListLoadState {
    data class Success(val songs: List<Song>, val name: String = ""): PlayListLoadState
    data object Loading: PlayListLoadState
    data object Error: PlayListLoadState
}

sealed interface PlaylistsUiState {
    data object Loading: PlaylistsUiState
    data object Error: PlaylistsUiState
    data class Success(val playlist: MutableList<PlaylistApiResponse>): PlaylistsUiState
}
