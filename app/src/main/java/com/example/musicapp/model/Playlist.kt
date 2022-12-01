package com.example.musicapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    var playlistId : Int,
    var playlistName : String
)
