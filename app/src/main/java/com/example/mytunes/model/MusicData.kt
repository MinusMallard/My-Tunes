package com.example.mytunes.model

import com.google.gson.annotations.SerializedName

data class MusicData(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: MusicDataDetails
)

data class MusicDataDetails(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("year") val year: Int?,
    @SerializedName("type") val type: String?,
    @SerializedName("playCount") val playCount: Int?,
    @SerializedName("language") val language: String?,
    @SerializedName("explicitContent") val explicitContent: Boolean,
    @SerializedName("songCount") val songCount: Int?,
    @SerializedName("url") val url: String?,
    @SerializedName("image") val image: List<Image>,
    @SerializedName("songs") val songs: List<Song>,
    @SerializedName("artists") val artists: List<PlaylistArtist>
)
data class PlaylistArtist(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("image") val image: List<Image>,
    @SerializedName("url") val url: String?
)