package com.example.mytunes.model

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.SerializedName

@Immutable
data class HomeApiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: Data
)

@Immutable
data class Data(
    @SerializedName("albums") val albums: List<HomeAlbum>,
    @SerializedName("playlists") val playlists: List<HomePlaylist>,
    @SerializedName("charts") val charts : List<Charts>,
    @SerializedName("trending") val trending : Trending

)

@Immutable
data class Charts (

    @SerializedName("id") val id : Int,
    @SerializedName("title") val title : String,
    @SerializedName("subtitle") val subtitle : String,
    @SerializedName("type") val type : String,
    @SerializedName("image") val image : List<Image>,
    @SerializedName("url") val url : String,
    @SerializedName("firstname") val firstname : String,
    @SerializedName("explicitContent") val explicitContent : Int,
    @SerializedName("language") val language : String
)

@Immutable
data class Trending (
    @SerializedName("songs") val songs : List<Song>,
    @SerializedName("albums") val albums : List<HomeAlbum>
)

@Immutable
data class HomeAlbum(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("year") val year: String,
    @SerializedName("type") val type: String,
    @SerializedName("playCount") val playCount: String,
    @SerializedName("language") val language: String,
    @SerializedName("explicitContent") val explicitContent: String,
    @SerializedName("songCount") val songCount: String,
    @SerializedName("url") val url: String,
    @SerializedName("primaryArtists") val primaryArtists: List<HomeArtist>,
    @SerializedName("featuredArtists") val featuredArtists: List<HomeArtist>,
    @SerializedName("artists") val artists: List<HomeArtist>,
    @SerializedName("image") val image: List<HomeImage>,
)

@Immutable
data class HomePlaylist(
    @SerializedName("id") val id: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("title") val title: String,
    @SerializedName("subtitle") val subtitle: String,
    @SerializedName("type") val type: String,
    @SerializedName("image") val image: List<HomeImage>,
    @SerializedName("url") val url: String,
    @SerializedName("songCount") val songCount: String,
    @SerializedName("firstname") val firstname: String,
    @SerializedName("followerCount") val followerCount: String,
    @SerializedName("lastUpdated") val lastUpdated: String,
    @SerializedName("explicitContent") val explicitContent: String
)

@Immutable
data class HomeArtist(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
    @SerializedName("type") val type: String,
    @SerializedName("role") val role: String
)

@Immutable
data class HomeImage(
    @SerializedName("quality") val quality: String,
    @SerializedName("link") val link: String
)

