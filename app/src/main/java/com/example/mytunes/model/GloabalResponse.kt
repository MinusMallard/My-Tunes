package com.example.mytunes.model

import com.google.gson.annotations.SerializedName

data class GlobalResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: GlobalData
)

data class GlobalData(
    @SerializedName("albums") val albums: Results<GlobalAlbum>?,
    @SerializedName("songs") val songs: Results<GlobalSong>?,
    @SerializedName("artists") val artists: Results<GlobalArtist>?,
    @SerializedName("playlists") val playlists: Results<GlobalPlaylist>?,
    @SerializedName("topQuery") val topQuery: Results<TopQuery>?
)

data class Results<T>(
    @SerializedName("results") val results: List<T>,
    @SerializedName("position") val position: Int
)

data class GlobalAlbum(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: List<Image>,
    @SerializedName("artist") val artist: String,
    @SerializedName("url") val url: String,
    @SerializedName("type") val type: String,
    @SerializedName("description") val description: String,
    @SerializedName("year") val year: String,
    @SerializedName("language") val language: String,
    @SerializedName("songIds") val songIds: String
)

data class GlobalSong(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: List<Image>,
    @SerializedName("album") val album: String,
    @SerializedName("url") val url: String,
    @SerializedName("type") val type: String,
    @SerializedName("description") val description: String,
    @SerializedName("primaryArtists") val primaryArtists: String,
    @SerializedName("singers") val singers: String,
    @SerializedName("language") val language: String
)
// used weird names like "GlobalArtist because of redeclaration issue as artist is already defined in "SongResponse"
data class GlobalArtist(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: List<Image>,
    @SerializedName("type") val type: String,
    @SerializedName("description") val description: String,
    @SerializedName("position") val position: Int
)

data class GlobalPlaylist(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: List<Image>,
    @SerializedName("url") val url: String,
    @SerializedName("language") val language: String,
    @SerializedName("type") val type: String,
    @SerializedName("description") val description: String
)

data class TopQuery(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: List<Image>,
    @SerializedName("album") val album: String,
    @SerializedName("url") val url: String,
    @SerializedName("type") val type: String,
    @SerializedName("description") val description: String,
    @SerializedName("primaryArtists") val primaryArtists: String,
    @SerializedName("singers") val singers: String,
    @SerializedName("language") val language: String
)
