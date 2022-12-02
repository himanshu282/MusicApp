package com.example.musicapp

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {
    private var INSTANCE: MusicDatabase? = null
    fun getInstance(context: Context): MusicDatabase? {
        if (INSTANCE == null) {
            synchronized(MusicDatabase::class) {
                INSTANCE = buildRoomDB(context)
            }
        }
        return INSTANCE
    }
    private fun buildRoomDB(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            MusicDatabase::class.java,
            "playlist-songs"
        ).build()
}