package com.example.mytunes.media

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.mytunes.R
import com.example.mytunes.model.Song

class PlaybackService : MediaSessionService()  {
    private var mediaSession: MediaSession? = null
    private lateinit var player: ExoPlayer

    companion object {
        const val CHANNEL_ID = "mytunes_channel"
        const val NOTIFICATION_ID = 1
    }

    // Create your player and media session in the onCreate lifecycle event
    override fun onCreate() {
        super.onCreate()

        player = ExoPlayer.Builder(this).build()

        mediaSession = MediaSession.Builder(this, player)
            .setId("MyTunes_MediaSession")
            .build()
    }

    // This example always accepts the connection request
    override fun onGetSession(
        controllerInfo: MediaSession.ControllerInfo
    ): MediaSession? = mediaSession

    // Remember to release the player and media session in onDestroy
    override fun onDestroy() {
        mediaSession?.release()
        player.release()
        mediaSession = null
        super.onDestroy()
    }

    // public functions to manage playlist (can be called from controller)

    fun setPlaylist(songs: List<Song>, startIndex: Int = 0) {
        val mediaItems: MutableList<MediaItem> = songs.map { it -> toMediaItem(it) }.toMutableList()
        player.setMediaItems(mediaItems, startIndex, 0L)
        player.prepare()
        player.play()
    }

    fun addToPlaylist(song: Song) {
        player.addMediaItem(toMediaItem(song))
    }

    fun removeFromPlaylist(index: Int) {
        player.removeMediaItem(index)
    }

    // ✅ Foreground Service with Notification
    private fun startForegroundService() {
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("MyTunes")
            .setContentText("Playing music")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your icon
            .setOngoing(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "MyTunes Playback",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun toMediaItem(it: Song): MediaItem {
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
        return mediaItem
    }
}