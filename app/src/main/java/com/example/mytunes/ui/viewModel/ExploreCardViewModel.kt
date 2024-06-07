package com.example.mytunes.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytunes.data.MyTunesDataRepository
import com.example.mytunes.model.Album
import com.example.mytunes.model.AlbumResponse
import com.example.mytunes.ui.screen.SearchScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException


class ExploreCardViewModel (
    private val myTunesDataRepository: MyTunesDataRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<ExploreCardUiState>(ExploreCardUiState.Loading)
    val uiState: StateFlow<ExploreCardUiState> = _uiState.asStateFlow()

    fun searchAlbums(query: String) {
        viewModelScope.launch {
            _uiState.update {
                try {
                    val response = myTunesDataRepository.getAlbums(query, 0, 40)
                    if (response.success) {
                        ExploreCardUiState.Success(
                            albums = response
                        )
                    } else {
                        ExploreCardUiState.Error
                }
                } catch (e : Exception) {
                    ExploreCardUiState.Error
                }
            }
        }
    }
}

sealed interface ExploreCardUiState {
    data object Loading: ExploreCardUiState

    data object Error: ExploreCardUiState
    data class Success(
        var albums: AlbumResponse
    ): ExploreCardUiState
}