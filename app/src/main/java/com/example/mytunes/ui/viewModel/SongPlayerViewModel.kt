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
import androidx.media3.common.Player.EVENT_MEDIA_ITEM_TRANSITION
import androidx.media3.common.Player.EVENT_TRACKS_CHANGED
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.mytunes.MyTunesApplication
import com.example.mytunes.media.PlaybackService
import com.example.mytunes.model.Song
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class SongPlayerViewModel @SuppressLint("StaticFieldLeak") constructor(
    private val application: MyTunesApplication
): ViewModel() {

    private val context = application.applicationContext


    private lateinit var controller: MediaController
    private lateinit var controllerFuture: ListenableFuture<MediaController>

    init {
        initializeController()
    }

    private fun initializeController() {
        val sessionToken = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        getEvents()
    }

    private fun getEvents() {
        viewModelScope.launch {
            try {
                controller = controllerFuture.await()
            } catch (t: Throwable) {
                Log.w("SongPlayerViewModel", "Failed to connect to MediaController", t)
            }

            controller.addListener(
                object : Player.Listener {
                    override fun onEvents(player: Player, events: Player.Events) {
                        if (events.contains(EVENT_TRACKS_CHANGED)) {
                            setCurrentIndex(controller.currentMediaItemIndex)
                            setCurrentSong()
                            _progress.value = 0f
                            returnNext()
                            returnPrev()
                        }
                        if (events.contains(Player.EVENT_IS_PLAYING_CHANGED)) {
                            _isPlaying.value = controller.isPlaying

                            startTrackingProgress()
                        }
                        if (events.contains(Player.EVENT_IS_LOADING_CHANGED)) {
                            startTrackingProgress()
                        }
                    }
                }
            )
        }
    }

    private val _next = MutableStateFlow<Song?>(null)
    val next: StateFlow<Song?> = _next.asStateFlow()

    private val _prev = MutableStateFlow<Song?>(null)
    val prev: StateFlow<Song?> = _prev.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _queue = MutableStateFlow<MutableList<Song>>(mutableListOf())
    val queue: StateFlow<MutableList<Song>> = _queue.asStateFlow()

    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong.asStateFlow()

    private val _isPlaying =  MutableStateFlow<Boolean>(false)
    val isPlaying : StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentIndex = MutableStateFlow<Int>(0)
    val currentIndex : StateFlow<Int> = _currentIndex.asStateFlow()

    private val _currentSongId = MutableStateFlow<String?>(null)
    val currentSongId: StateFlow<String?> = _currentSongId.asStateFlow()

    fun removeSongFromQueue(song: Song) {
        _queue.value -= song
    }

    fun pause() {
        _isPlaying.value = false
        controllerFuture.addListener(
            {
                val a = controllerFuture.get()
                a.pause()
            },
            MoreExecutors.directExecutor()
        )
    }

    fun play() {
        _isPlaying.value = true
        controllerFuture.addListener(
            {
                val a = controllerFuture.get()
                a.prepare()
                a.play()
            },
            MoreExecutors.directExecutor()
        )
    }

    fun playNextSong() {
        controllerFuture.addListener(
            {
                val a = controllerFuture.get()
                a.seekToNext()
                a.prepare()
                a.play()
                _isPlaying.value = true
                setCurrentIndex(a.currentMediaItemIndex)
                setCurrentSong()
            },
            MoreExecutors.directExecutor()
        )
    }
    fun playPreviousSong() {
        controllerFuture.addListener(
            {
                val a = controllerFuture.get()
                a.seekToPrevious()
                a.prepare()
                a.play()
                _isPlaying.value = true
                setCurrentIndex(a.currentMediaItemIndex)
                setCurrentSong()
            },
            MoreExecutors.directExecutor()
        )

    }

    fun addSongList(songs: MutableList<Song>) {
        Log.d("song", songs[0].url)
        _queue.value.clear()
        _queue.value.addAll(songs)
        controllerFuture.addListener(
            {
                val a = controllerFuture.get()
                a.clearMediaItems()
                for (it in _queue.value) {
                    val mediaItem = MediaItem
                        .Builder()
                        .setMediaId(it.id)
                        .setUri((it.downloadUrl[it.downloadUrl.size-1].url))
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setArtist(it.artists.primary[0].name)
                                .setTitle(it.name)
                                .setArtworkUri(Uri.parse(it.image[2].url))
                                .build()
                        ).build()
                    a.addMediaItem(mediaItem)
                }
                a.seekTo(_currentIndex.value, 0L)
                a.prepare()
                a.play()
                _isPlaying.value = true
            }
            , MoreExecutors.directExecutor()
        )
        setCurrentSong()
    }

    fun setCurrentSong() {
        _currentSong.value = _queue.value[_currentIndex.value]
    }

    fun setCurrentIndex(index: Int) {
        _currentIndex.value = index
    }

    fun getTotalDuration(): Long {
        var total: Long = 0L
        viewModelScope.launch {
            controllerFuture.addListener(
                {
                    val a = controllerFuture.get()
                    total = a.duration
                },
                MoreExecutors.directExecutor()
            )
        }
        return total
    }

    fun getCurrentPosition(): Long {
        var curr: Long = 0L
        viewModelScope.launch {
            controllerFuture.addListener(
                {
                    val a = controllerFuture.get()
                    curr = a.contentBufferedPosition
                },
                MoreExecutors.directExecutor()
            )
        }
        return curr
    }

    fun startTrackingProgress() {
            viewModelScope.launch {
                controller = controllerFuture.await()
                while (controller.isPlaying) {
                    val currentPosition = controller.currentPosition.toFloat()
                    val duration = controller.duration.toFloat()
                    if (duration > 0) {
                        _progress.value = currentPosition / duration
                    }
                    delay(100)
                }
            }
    }

    fun seekTo(position: Float) {
        _progress.value = position
        viewModelScope.launch {
            controller = controllerFuture.await()
            val a = position * controller.duration
            controller.seekTo(a.toLong())
        }
    }

    fun returnNext() {
        var a: Int = -1
        viewModelScope.launch {
            controller = controllerFuture.await()
            a = controller.currentMediaItemIndex
        }
        if (a < _queue.value.size - 1) {
            a += 1
        }
        _next.value = queue.value[a]
    }

    fun returnPrev() {
        var a: Int = queue.value.size - 1
        viewModelScope.launch {
            controller = controllerFuture.await()
            a = controller.currentMediaItemIndex
        }
        if (a > 0) {
            a -= 1
        }
        _prev.value = queue.value[a]
    }
}