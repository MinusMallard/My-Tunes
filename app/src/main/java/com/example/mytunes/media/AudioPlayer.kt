package com.example.mytunes.media

import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class PlaybackService : MediaSessionService()  {

    private lateinit var player: ExoPlayer
    private var mediaSession: MediaSession? = null


    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer
            .Builder(this)
            .setHandleAudioBecomingNoisy(true)
            .build()
//        val mediaItem = MediaItem.fromUri("https://aac.saavncdn.com/322/935ec81cd6eaccf69bec839dc72b0802_320.mp4")
//        player.setMediaItem(mediaItem)
        player.prepare()
        mediaSession = MediaSession.Builder(this, player).build()
    }

    fun play() {
        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun seekTo(time: Long) {
        player.seekTo(time)
    }

    fun seekToNextMediaItem() {
        player.seekToNextMediaItem()
    }

    fun seekToPreviousMediaItem() {
        player.seekToPreviousMediaItem()
    }



    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    override fun onGetSession(
        controllerInfo: MediaSession.ControllerInfo
    ): MediaSession? = mediaSession


}