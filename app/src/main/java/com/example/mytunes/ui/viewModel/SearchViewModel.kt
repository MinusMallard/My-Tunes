package com.example.mytunes.ui.viewModel

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mytunes.data.MyTunesDataRepository
import com.example.mytunes.ui.screen.SearchScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import android.util.Log

class SearchViewModel(
    private val myTunesDataRepository: MyTunesDataRepository
): ViewModel()
{
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _response = MutableStateFlow<SearchScreenState>(SearchScreenState.Nothing)
    val response: StateFlow<SearchScreenState> = _response.asStateFlow()

    private val _searchType = MutableStateFlow("songs")
    val searchType: StateFlow<String> = _searchType.asStateFlow()

    private val _selected = MutableStateFlow(1)
    val selected: StateFlow<Int> = _selected.asStateFlow()

    fun updateSearchString(text: String) {
        _searchText.update { text }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun changeSearchType(type: String) {
        _searchType.update { type }
        search()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun search() {
        if (_searchText.value.isBlank()) return
        _response.value = SearchScreenState.Loading
        viewModelScope.launch {
                _response.update  {
                    try {
                        if (searchType.value == "songs") {
                            val response = myTunesDataRepository.getSongs(searchText.value, 0, 40)
                            if (response.success) {
                                SearchScreenState.SuccessSong(
                                    searchSongs = response
                                )
                            }
                            else {
                                SearchScreenState.Error
                            }
                        } else if (searchType.value == "albums") {
                            val response = myTunesDataRepository.getAlbums(searchText.value, 0, 40)
                            if (response.success) {
                                SearchScreenState.SuccessAlbum(
                                    searchAlbums = response
                                )
                            }
                            else {
                                SearchScreenState.Error
                            }
                        }
                        else {
                            val response = myTunesDataRepository.getPlaylists(searchText.value, 0, 40)
                            if (response.success) {
                                SearchScreenState.SuccessPlaylist(
                                    searchPlaylists = response
                                )
                            }
                            else {
                                SearchScreenState.Error
                            }
                        }
                    }
                    catch (e: IOException) {
                        SearchScreenState.Error
                    } catch (e: HttpException) {
                        SearchScreenState.Error
                    } catch (e: Exception) {
                        SearchScreenState.Error
                    }

                }
        }
    }

    fun reset() {
        _response.update {
            SearchScreenState.Nothing
        }
        _searchText.update {
            ""
        }
    }
}