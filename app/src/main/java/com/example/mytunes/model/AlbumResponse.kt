package com.example.mytunes.model

import com.google.gson.annotations.SerializedName

data class AlbumResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: Albums
)

data class Albums(
    @SerializedName("total") val total: Int,
    @SerializedName("start") val start: Int,
    @SerializedName("results") val results: List<Album>
)

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

data class AlbumArtists(
    @SerializedName("primary") val primary: List<Artist>?,
    @SerializedName("featured") val featured: List<Artist>?,
    @SerializedName("all") val all: List<Artist>?
)

data class AlbumImage(
    @SerializedName("quality") val quality: String?, // Nullable because it can be null in the JSON
    @SerializedName("url") val url: String? // Nullable because it can be null in the JSON
)