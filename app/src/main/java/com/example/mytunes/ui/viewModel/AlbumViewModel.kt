package com.example.mytunes.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytunes.data.MyTunesDataRepository
import com.example.mytunes.model.ApiResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface AlbumUiState {
    data object Loading: AlbumUiState
    data object Error: AlbumUiState
    data class Success(val albumList: ApiResponse): AlbumUiState
}
class AlbumViewModel(
    private val myTunesDataRepository: MyTunesDataRepository
): ViewModel()
{
    private val _albumUiState = MutableStateFlow<AlbumUiState>(AlbumUiState.Loading)
    val albumUiState: StateFlow<AlbumUiState> = _albumUiState.asStateFlow()

    fun loadAlbum(id: String) {
        viewModelScope.launch {
            _albumUiState.update {
                try {
                    val response = myTunesDataRepository.getAlbum(id)
                    if (response.success) {
                        AlbumUiState.Success(response)
                    } else {
                        AlbumUiState.Error
                    }
                } catch (e: Exception) {
                    AlbumUiState.Error
                }
            }
        }
    }
}