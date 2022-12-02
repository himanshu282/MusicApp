package com.example.musicapp

import android.app.Application

class MyApplication  : Application() {

    var database : MusicDatabase? = null
    override fun onCreate() {
        super.onCreate()
        database = DatabaseBuilder.getInstance(this)

    }
}

