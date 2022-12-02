package com.example.musicapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Song(
    @PrimaryKey(autoGenerate = true)
    val songId : Int = 0,
    val playlistName : String,
    val songUrl: String,
    val songName: String,
    val singerName: String
)
