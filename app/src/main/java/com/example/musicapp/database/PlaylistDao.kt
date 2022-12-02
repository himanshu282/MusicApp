package com.example.musicapp.database

import androidx.room.*
import com.example.musicapp.model.Playlist
import com.example.musicapp.model.Song
import com.example.musicapp.model.relations.PlaylistWithSongs

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: Playlist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: Song)

    @Transaction
    @Query("SELECT * FROM song where playlistName = :playlistName")
    suspend fun getPlaylistWithSongs(playlistName : String) : PlaylistWithSongs?

    @Query("SELECT * FROM playlist")
    suspend fun getPlaylist() : List<Playlist>?

}