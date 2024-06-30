package com.example.mytunes.data


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
import com.example.mytunes.network.MyTunesApiService

interface MyTunesDataRepository {
    suspend fun searchAll(endpoint: String): GlobalResponse

    suspend fun getSongs(search: String, page: Int, limit: Int): SongResponse

    suspend fun getAlbums(search: String, page: Int, limit: Int): AlbumResponse

    suspend fun getPlaylists(search: String, page: Int, limit: Int): PlaylistResponse
    suspend fun getArtists(search: String, page: Int, limit: Int): ArtistResponse

    suspend fun getSong(id: String): Song

    suspend fun getLyrics(id: String): Lyrics

    suspend fun getAlbum(id: String): ApiResponse

    suspend fun getPlaylist(id: String): PlaylistApiResponse

    suspend fun getHomePageData(languages: String): HomeApiResponse

}

class NetworkDataRepository (
    private val myTunesApiService: MyTunesApiService
) : MyTunesDataRepository {
    override suspend fun searchAll(endpoint: String): GlobalResponse = myTunesApiService.searchAll(endpoint)

    override suspend fun getSongs(search: String, page: Int, limit: Int): SongResponse = myTunesApiService.getSongs(search, page, limit)

    override suspend fun getAlbums(search: String, page: Int, limit: Int): AlbumResponse = myTunesApiService.getAlbums(search, page, limit)

    override suspend fun getPlaylists(search: String, page: Int, limit: Int): PlaylistResponse = myTunesApiService.getPlaylists(search, page, limit)

    override suspend fun getArtists(search: String, page: Int, limit: Int): ArtistResponse = myTunesApiService.getArtists(search, page, limit)

    override suspend fun getSong(id: String): Song = myTunesApiService.getSong(id)

    override suspend fun getLyrics(id: String): Lyrics = myTunesApiService.getLyrics(id)

    override suspend fun getAlbum(id: String): ApiResponse = myTunesApiService.getAlbum(id)

    override suspend fun getPlaylist(id: String): PlaylistApiResponse = myTunesApiService.getPlaylist(id)

    override suspend fun getHomePageData(languages: String): HomeApiResponse = myTunesApiService.getHomePageData(languages)
}