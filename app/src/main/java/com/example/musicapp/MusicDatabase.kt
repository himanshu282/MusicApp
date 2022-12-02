package com.example.musicapp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.musicapp.database.PlaylistDao
import com.example.musicapp.model.Playlist
import com.example.musicapp.model.Song


@Database(
    entities = [Playlist::class, Song::class],
    version = 1)
abstract class MusicDatabase : RoomDatabase(){
    abstract fun playlistDao() : PlaylistDao
}