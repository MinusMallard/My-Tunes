package com.example.mytunes.network

import com.example.mytunes.model.AlbumResponse
import com.example.mytunes.model.ApiResponse
import com.example.mytunes.model.ArtistResponse
import com.example.mytunes.model.GlobalResponse
import com.example.mytunes.model.HomeApiResponse
import com.example.mytunes.model.Lyrics
import com.example.mytunes.model.MusicData
import com.example.mytunes.model.PlaylistApiResponse
import com.example.mytunes.model.PlaylistResponse
import com.example.mytunes.model.Song
import com.example.mytunes.model.SongResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MyTunesApiService {

    @GET("api/search")
    suspend fun searchAll(
        @Query("query") search: String,
        ): GlobalResponse

    @GET("api/search/songs")
    suspend fun getSongs(
        @Query("query") song: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): SongResponse

    @GET("api/search/albums")
    suspend fun getAlbums(
        @Query("query") song: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): AlbumResponse

    @GET("api/search/playlists")
    suspend fun getPlaylists(
        @Query("query") song: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): PlaylistResponse

    @GET("api/search/artists")
    suspend fun getArtists(
        @Query("query") song: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ArtistResponse

    @GET("api/songs/{id}")
    suspend fun getSong(
        @Path("id") id: String,
    ): Song

    @GET("api/songs/{id}/lyrics")
    suspend fun getLyrics(
        @Path("id") id: String
    ): Lyrics

    @GET("api/albums")
    suspend fun getAlbum(
        @Query("id") id: String,
    ): ApiResponse

    @GET("api/playlists")
    suspend fun getPlaylist(
        @Query("id") id: String,
        @Query("limit") limit: Int = 100,
    ): PlaylistApiResponse



    @GET("modules")
    suspend fun getHomePageData(
        @Query("language") languages: String,
    ): HomeApiResponse
}