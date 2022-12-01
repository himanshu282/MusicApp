package com.example.musicapp.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.musicapp.Playlist
import com.example.musicapp.model.Song

data class PlaylistWithSongs(
    @Embedded val article: Playlist,
    @Relation(
        parentColumn = "title",
        entityColumn = "title"
    )
    val images : List<Song>
)
