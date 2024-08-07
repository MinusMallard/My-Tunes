package com.example.mytunes.model

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class ArtistResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: ArtistData
)

@Immutable
data class ArtistData(
    @SerializedName("total") val total: Int,
    @SerializedName("start") val start: Int,
    @SerializedName("results") val results: List<Artist>
)

@Immutable
data class Artist(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("role") val role: String,
    @SerializedName("type") val type: String,
    @SerializedName("image") val image: List<ArtistImage>,
    @SerializedName("url") val url: String
)

@Immutable
data class ArtistImage(
    @SerializedName("quality") val quality: String?,
    @SerializedName("url") val url: String?
)