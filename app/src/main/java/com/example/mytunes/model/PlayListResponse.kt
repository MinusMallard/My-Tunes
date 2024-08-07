package com.example.mytunes.model

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class PlaylistResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: DataPlaylist,
)

@Immutable
data class DataPlaylist(
    @SerializedName("total") val total: Int,
    @SerializedName("start") val start: Int,
    @SerializedName("results") val results: List<Playlist>
)

@Immutable
data class Playlist(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("language") val language: String?,
    @SerializedName("explicitContent") val explicitContent: Boolean,
    @SerializedName("songCount") val songCount: Int?,
    @SerializedName("url") val url: String?,
    @SerializedName("image") val image: List<Image>,
)

@Immutable
data class Image(
    @SerializedName("quality") val quality: String?,
    @SerializedName("url") val url: String?
)
