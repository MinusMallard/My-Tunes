package com.example.mytunes.model

import android.provider.MediaStore
import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class AlbumResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: Albums
)

@Immutable
data class Albums(
    @SerializedName("total") val total: Int,
    @SerializedName("start") val start: Int,
    @SerializedName("results") val results: List<Album>
)

@Immutable
data class Album(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("year") val year: Int?, // Nullable because it can be null in the JSON
    @SerializedName("type") val type: String,
    @SerializedName("playCount") val playCount: Int?, // Nullable because it can be null in the JSON
    @SerializedName("language") val language: String,
    @SerializedName("explicitContent") val explicitContent: Boolean,
    @SerializedName("artists") val artists: AlbumArtists,
    @SerializedName("url") val url: String,
    @SerializedName("image") val image: List<AlbumImage>
)

@Immutable
data class AlbumArtists(
    @SerializedName("primary") val primary: List<Artist>?,
    @SerializedName("featured") val featured: List<Artist>?,
    @SerializedName("all") val all: List<Artist>?
)

@Immutable
data class AlbumImage(
    @SerializedName("quality") val quality: String?, // Nullable because it can be null in the JSON
    @SerializedName("url") val url: String? // Nullable because it can be null in the JSON
)

@Immutable
data class ApiResponse(
    val success: Boolean,
    val data: AlbumData
)

@Immutable
data class AlbumData(
    val id: String,
    val name: String,
    val description: String,
    val type: String,
    val year: Int?,
    val playCount: Int?,
    val language: String,
    val explicitContent: Boolean,
    val url: String,
    val songCount: Int,
    val artists: Artist,
    val image: List<Image>,
    val songs: List<Song>
)

@Immutable
data class PlaylistApiResponse(
    val success: Boolean,
    val data: PlaylistData
)

@Immutable
data class PlaylistData(
    val id: String,
    val name: String,
    val description: String?,
    val type: String,
    val year: Int?,
    val playCount: Int?,
    val language: String,
    val explicitContent: Boolean,
    val url: String,
    val songCount: Int?,
    val image: List<Image>,
    val songs: List<Song>
)
