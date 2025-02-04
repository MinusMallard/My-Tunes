package com.example.mytunes.model

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class SongResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: DataSong
)

@Immutable
data class DataSong (
    @SerializedName("total") val total: Int,
    @SerializedName("start") val start: Int,
    @SerializedName("results") val results: List<Song>
)

@Immutable
data class Song(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("year") val year: Int?,
    @SerializedName("releaseDate") val releaseDate: String?,
    @SerializedName("duration") val duration: Int?,
    @SerializedName("label") val label: String?,
    @SerializedName("explicitContent") val explicitContent: Boolean,
    @SerializedName("playCount") val playCount: Int?,
    @SerializedName("language") val language: String,
    @SerializedName("hasLyrics") val hasLyrics: Boolean,
    @SerializedName("lyricsId") val lyricsId: String?,

    @SerializedName("url") val url: String,
    @SerializedName("copyright") val copyright: String?,
    @SerializedName("album") val album: SongAlbum,
    @SerializedName("artists") val artists: SongArtists,
    @SerializedName("image") val image: List<SongImage>,
    @SerializedName("downloadUrl") val downloadUrl: List<DownloadUrl>
)

@Immutable
data class Lyrics(
    @SerializedName("lyrics") val lyrics: String,
    @SerializedName("copyright") val copyright: String?,
    @SerializedName("snippet") val snippet: String
)

@Immutable
data class SongAlbum(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("url") val url: String?
)

@Immutable
data class SongArtists(
    @SerializedName("primary") val primary: List<Artist>,
    @SerializedName("featured") val featured: List<Artist>,
    @SerializedName("all") val all: List<Artist>
)

@Immutable
data class SongImage(
    @SerializedName("quality") val quality: String,
    @SerializedName("url") val url: String
)

@Immutable
data class DownloadUrl(
    @SerializedName("quality") val quality: String,
    @SerializedName("url") val url: String
)
