package com.example.musicapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.musicapp.MyApplication
import com.example.musicapp.Repository
import com.example.musicapp.model.Playlist
import com.example.musicapp.model.Song
import com.example.musicapp.model.Songs
import kotlinx.coroutines.launch

class PlaylistSharedViewModel(application: Application) : AndroidViewModel(application) {
    private var repository :Repository = Repository((application as MyApplication).database)
    var playlist = MutableLiveData<List<Playlist>?>()
    var song  = MutableLiveData<Songs>()



    fun insertSong(song: Song){
        viewModelScope.launch {
            repository.insertSong(song)
        }
    }

    fun insertPlaylist(playlist: Playlist){
        viewModelScope.launch {
            repository.insertPlaylist(playlist)
        }
    }

     fun getPlaylist(){
        viewModelScope.launch {
           playlist.postValue(repository.getPlaylist())
        }
    }

}