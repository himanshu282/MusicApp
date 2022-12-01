package com.example.musicapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistSharedViewModel : ViewModel() {
    val playlistName = MutableLiveData<String>()

    fun addPlaylistName(name : String){
        playlistName.value = name
    }


}