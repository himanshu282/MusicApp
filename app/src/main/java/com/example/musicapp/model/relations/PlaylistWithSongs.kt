package com.example.musicapp.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.musicapp.model.Song

data class PlaylistWithSongs(
    @Embedded val playlist: com.example.musicapp.model.Playlist,
    @Relation(
        parentColumn = "playlistName",
        entityColumn = "playlistName"
    )
    val songs : List<Song>
)
