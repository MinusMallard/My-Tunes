package com.example.mytunes.ui.viewModel

import android.annotation.SuppressLint
import android.content.ComponentName
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.mytunes.MyTunesApplication
import com.example.mytunes.media.PlaybackService
import com.example.mytunes.model.Song
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class SongPlayerViewModel(
    private val application: MyTunesApplication
) : ViewModel() {

    private val context = application.applicationContext

    private lateinit var controller: MediaController

    private val _queue = MutableStateFlow<List<Song>>(emptyList())
    val queue: StateFlow<List<Song>> = _queue.asStateFlow()

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private var progressJob: Job? = null

    init {
        initializeController()
    }

    private fun initializeController() {
        viewModelScope.launch {
            try {
                val sessionToken = SessionToken(
                    context,
                    ComponentName(context, PlaybackService::class.java)
                )
                controller = MediaController.Builder(context, sessionToken).buildAsync().await()

                setupPlayerListeners()
                updateStateFromController()

            } catch (e: Exception) {
                Log.e("SongPlayerViewModel", "Failed to connect MediaController", e)
            }
        }
    }

    private fun setupPlayerListeners() {
        controller.addListener(
            object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    if (events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION) ||
                        events.contains(Player.EVENT_TRACKS_CHANGED)) {
                        updateCurrentSong()
                        _currentIndex.value = controller.currentMediaItemIndex
                        _progress.value = 0f
                    }

                    if (events.contains(Player.EVENT_IS_PLAYING_CHANGED)) {
                        _isPlaying.value = controller.isPlaying
                        if (controller.isPlaying) {
                            startTrackingProgress()
                        } else {
                            stopTrackingProgress()
                        }
                    }
                }
            }
        )
    }

    private fun updateStateFromController() {
        _isPlaying.value = controller.isPlaying
        _currentIndex.value = controller.currentMediaItemIndex
        updateCurrentSong()
    }

    private fun updateCurrentSong() {
        val index = controller.currentMediaItemIndex
        _currentSong.value = _queue.value.getOrNull(index)
    }

    // ✅ Public Playback Controls

    fun play() {
        controller.play()
    }

    fun pause() {
        controller.pause()
    }

    fun playNext() {
        controller.seekToNext()
    }

    fun playPrevious() {
        controller.seekToPrevious()
    }

    fun seekTo(position: Float) {
        val targetPosition = (position * controller.duration).toLong()
        controller.seekTo(targetPosition)
        _progress.value = position
    }

    // ✅ Playlist / Queue Management

    fun setQueue(songs: List<Song>, startIndex: Int = 0) {
        _queue.value = songs
        val mediaItems = songs.map { it.toMediaItem() }

        controller.setMediaItems(mediaItems, startIndex, 0L)
        controller.prepare()
        controller.play()

        _currentIndex.value = startIndex
        _currentSong.value = songs.getOrNull(startIndex)
    }

    fun removeSong(song: Song) {
        val updatedQueue = _queue.value.toMutableList().apply { remove(song) }
        _queue.value = updatedQueue

        controller.setMediaItems(updatedQueue.map { it.toMediaItem() })
        controller.prepare()
    }

    // ✅ Progress Tracking

    private fun startTrackingProgress() {
        if (progressJob?.isActive == true) return

        progressJob = viewModelScope.launch {
            while (controller.isPlaying) {
                val currentPosition = controller.currentPosition.toFloat()
                val duration = controller.duration.coerceAtLeast(1).toFloat()
                _progress.value = currentPosition / duration
                delay(500)
            }
        }
    }

    private fun stopTrackingProgress() {
        progressJob?.cancel()
        progressJob = null
    }

    // ✅ Helper Extension to Convert Song to MediaItem

    private fun Song.toMediaItem(): MediaItem {
        return MediaItem.Builder()
            .setMediaId(id)
            .setUri(downloadUrl.last().url)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(name)
                    .setArtist(artists.primary.firstOrNull()?.name ?: "Unknown Artist")
                    .setArtworkUri(Uri.parse(image.getOrNull(2)?.url))
                    .build()
            )
            .build()
    }

    override fun onCleared() {
        super.onCleared()
        stopTrackingProgress()
        controller.release()
    }

    fun playNextSong() = playNext()

    fun playPreviousSong() = playPrevious()

    fun addSongList(songs: MutableList<Song>, startIndex: Int = 0) = setQueue(songs, startIndex)

    fun removeSongFromQueue(song: Song) = removeSong(song)
}