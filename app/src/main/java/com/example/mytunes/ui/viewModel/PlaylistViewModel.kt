package com.example.mytunes.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytunes.data.MyTunesDataRepository
import com.example.mytunes.model.ApiResponse
import com.example.mytunes.model.PlaylistApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface PlaylistUiState {
    data object Loading: PlaylistUiState
    data object Error: PlaylistUiState
    data class Success(val playlist: PlaylistApiResponse): PlaylistUiState
}
class PlaylistViewModel(
    private val myTunesDataRepository: MyTunesDataRepository
): ViewModel() {

    private val _playlistUiState = MutableStateFlow<PlaylistUiState>(PlaylistUiState.Loading)
    val playlistUiState: StateFlow<PlaylistUiState> = _playlistUiState.asStateFlow()

    fun loadPlaylist(id: String) {
        viewModelScope.launch {
            _playlistUiState.update {
                try {
                    val response = myTunesDataRepository.getPlaylist(id)
                    if (response.success) {
                        PlaylistUiState.Success(response)
                    } else {
                        PlaylistUiState.Error
                    }
                } catch (e: Exception) {
                    PlaylistUiState.Error
                }
            }
        }
    }
}