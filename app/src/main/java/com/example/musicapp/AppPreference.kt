package com.example.musicapp

import android.content.Context
import android.content.SharedPreferences

object AppPreference {

    private const val NAME = "APP_PREFERENCE"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val PLAYLIST_INFO_CACHED = Pair("Playlist_Info", "")

    fun init(context: Context) {
        preferences = context.getSharedPreferences(
            NAME,
            MODE
        )
        editor = preferences.edit()
    }



    var listOfPlaylist : String?
    get() = preferences.getString(PLAYLIST_INFO_CACHED.first, PLAYLIST_INFO_CACHED.second)
    set(value) {
            editor.putString(PLAYLIST_INFO_CACHED.first, value)
            editor.apply()
    }



}