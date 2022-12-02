package com.example.musicapp

import com.example.musicapp.model.Playlist
import com.example.musicapp.model.Song
import com.example.musicapp.model.relations.PlaylistWithSongs

class Repository(musicDatabase: MusicDatabase?) {
    private val playlistDao = musicDatabase?.playlistDao()


    suspend fun getPlaylistWithSongs(playlistName : String) : PlaylistWithSongs? {
        return playlistDao?.getPlaylistWithSongs(playlistName)
    }

    suspend fun insertPlaylist(playlist: Playlist): Unit? {
        return playlistDao?.insertPlaylist(playlist)
    }

    suspend fun insertSong(song: Song): Unit? {
        return playlistDao?.insertSong(song)
    }

    suspend fun getPlaylist() : List<Playlist>? {
        return playlistDao?.getPlaylist()
    }

}